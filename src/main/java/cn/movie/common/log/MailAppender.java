package cn.movie.common.log;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.boolex.OnErrorEvaluator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluator;
import ch.qos.logback.core.helpers.CyclicBuffer;
import ch.qos.logback.core.sift.DefaultDiscriminator;
import ch.qos.logback.core.sift.Discriminator;
import ch.qos.logback.core.spi.CyclicBufferTracker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Marker;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用 消息中心 发送日志邮件
 * <p>
 * 必填配置：
 * 名称	            类型	            说明
 * contentLayout    {@link Layout}  邮件正文版式
 * enabled          boolean         是否启用
 * url	            String	        API 链接
 * client_id	      String	        客户端 ID，由邮件中心提供
 * template_name	  String	        指定模板，basic
 * data	            String	        模板占位符键值对，json 格式，例如 {"content": "测试邮件"}
 * from	            String	        发件人
 * to	              String	        收件人
 * subject          String          邮件标题样式
 * <p>
 * 非必填配置：
 * 名称	            类型	            说明
 * asyncSend        boolean         是否异步发送请求
 * charset          String          字符编码
 *
 */
public class MailAppender extends AppenderBase<ILoggingEvent> {
  /**
   * 处理 Json
   */
  private static ObjectMapper objectMapper = new ObjectMapper();
  /**
   * 最大延迟 15天
   */
  private static final long MAX_DELAY_BETWEEN_STATUS_MESSAGES = 15 * 24 * 60 * 60 * CoreConstants.MILLIS_IN_ONE_SECOND;
  private long lastTrackerStatusPrint = 0;
  private long delayBetweenStatusMessages = 300 * CoreConstants.MILLIS_IN_ONE_SECOND;
  /**
   * 标题版式
   * 默认的标题模式
   * 正文版式(从配置文件获取，必填)
   */
  private Layout<ILoggingEvent> subjectLayout;
  private static final String DEFAULT_SUBJECT_PATTERN = "%logger{20} - %m";
  private Layout<ILoggingEvent> contentLayout;
  /**
   * 必填配置，需生成 Getter & Setter
   */
  private boolean enabled;
  private String url;
  private String clientId;
  private String templateName;
  private String from;
  private String to;
  private String subject;
  /**
   * 非必填配置，需生成 Getter & Setter
   */
  private boolean asyncSend = true;
  private String charset = "UTF-8";

  private int errorCount = 0;
  private EventEvaluator<ILoggingEvent> eventEvaluator;
  private Discriminator<ILoggingEvent> discriminator = new DefaultDiscriminator<>();
  private CyclicBufferTracker<ILoggingEvent> cbTracker;

  public MailAppender() {
  }

  /**
   * Start the appender
   */
  @Override
  public void start() {
    if (!enabled) {
      return;
    }
    if (cbTracker == null) {
      cbTracker = new CyclicBufferTracker<>();
    }
    if (eventEvaluator == null) {
      OnErrorEvaluator onError = new OnErrorEvaluator();
      onError.setContext(getContext());
      onError.setName("onError");
      onError.start();
      this.eventEvaluator = onError;
    }
    boolean invalid = isEmptyString(url) || isEmptyString(clientId)
        || isEmptyString(templateName) || isEmptyString(from)
        || isEmptyString(to) || isEmptyString(subject);
    if (invalid) {
      addError("The properties are invalid. Cannot start.");
      return;
    }
    subjectLayout = makeSubjectLayout(subject);
    started = true;
  }

  /**
   * 由事件触发，并构建邮件，发送给消息中心
   *
   * @param eventObject
   */
  @Override
  public void append(ILoggingEvent eventObject) {
    if (!checkEntryConditions()) {
      return;
    }

    String key = discriminator.getDiscriminatingValue(eventObject);
    long now = System.currentTimeMillis();
    final CyclicBuffer<ILoggingEvent> cyclicBuf = cbTracker.getOrCreate(key, now);

    //adding the event to cyclic buffer
    eventObject.prepareForDeferredProcessing();
    cyclicBuf.add(eventObject);

    try {
      if (eventEvaluator.evaluate(eventObject)) {
        //克隆 cyclicBuf
        CyclicBuffer<ILoggingEvent> clonedCyclicBuf = new CyclicBuffer<>(cyclicBuf);
        cyclicBuf.clear();
        if (asyncSend) {
          //异步发送
          SenderRunnable senderRunnable = new SenderRunnable(clonedCyclicBuf, eventObject);
          context.getScheduledExecutorService().execute(senderRunnable);
        } else {
          //同步发送
          sendMail(clonedCyclicBuf, eventObject);
        }
      }
    } catch (EvaluationException e) {
      errorCount++;
      if (errorCount < CoreConstants.MAX_ERROR_COUNT) {
        addError("MailAppender's EventEvaluator threw an Exception", e);
      }
    }

    //根据指定 marks 移除 key
    if (eventMarksEndOfLife(eventObject)) {
      cbTracker.endOfLife(key);
    }

    cbTracker.removeStaleComponents(now);

    if (lastTrackerStatusPrint + delayBetweenStatusMessages < now) {
      addInfo("MailAppender [" + name + "] is tracking [" + cbTracker.getComponentCount() + "] buffers");
      lastTrackerStatusPrint = now;
      // 当小于最大延迟值时，4 x delay
      if (delayBetweenStatusMessages < MAX_DELAY_BETWEEN_STATUS_MESSAGES) {
        delayBetweenStatusMessages *= 4;
      }
    }
  }

  /**
   * 检查条件是否满足
   *
   * @return boolean
   */
  private boolean checkEntryConditions() {
    if (!this.started) {
      addError("Attempting to append to a non-started appender: " + this.getName());
      return false;
    }
    return true;
  }

  /**
   * 构建邮件标题版式
   *
   * @param subject
   * @return Layout
   */
  private Layout<ILoggingEvent> makeSubjectLayout(String subject) {
    if (subject == null) {
      subject = DEFAULT_SUBJECT_PATTERN;
    }
    PatternLayout patternLayout = new PatternLayout();
    patternLayout.setContext(getContext());
    patternLayout.setPattern(subject);
    patternLayout.setPostCompileProcessor(null);
    patternLayout.start();
    return patternLayout;
  }

  /**
   * 如果包含 "FINALIZE_SESSION" 的 Marker，返回 true
   *
   * @param eventObject
   * @return boolean
   */
  private boolean eventMarksEndOfLife(ILoggingEvent eventObject) {
    Marker marker = eventObject.getMarker();
    return marker != null && marker.contains(ClassicConstants.FINALIZE_SESSION_MARKER);
  }

  /**
   * 发送邮件
   * 邮件系统接口的必填参数有：
   * 名称	            类型	        说明
   * client_id	      String	    客户端 ID，由邮件中心提供
   * template_name	  String	    指定模板，basic
   * subject	        String	    主题
   * data	            String	    模板占位符键值对，json 格式，例如 {"content": "测试邮件"}
   * from	            String	    发件人
   * to	              String	    收件人
   *
   * @param cyclicBuf
   * @param lastEvent
   */
  private void sendMail(CyclicBuffer<ILoggingEvent> cyclicBuf, ILoggingEvent lastEvent) {
    try {
      StringBuffer contentBuf = new StringBuffer();

      //<table> & <tr></tr> & <td></td> & etc
      String presentationHeader = contentLayout.getPresentationHeader();
      if (presentationHeader != null) {
        contentBuf.append(presentationHeader);
      }
      int len = cyclicBuf.length();
      for (int i = 0; i < len; i++) {
        ILoggingEvent event = cyclicBuf.get();
        contentBuf.append(contentLayout.doLayout(event));
      }

      //</table>
      String presentationFooter = contentLayout.getPresentationFooter();
      if (presentationFooter != null) {
        contentBuf.append(presentationFooter);
      }

      String subjectStr = "Undefined subject";
      if (subjectLayout != null) {
        subjectStr = subjectLayout.doLayout(lastEvent);

        // 标题不能包含换行符，如包含，从换行符处截断
        int newLinePos = (subjectStr != null) ? subjectStr.indexOf('\n') : -1;
        if (newLinePos > -1) {
          subjectStr = subjectStr.substring(0, newLinePos);
        }
      }

      //邮件参数
      Map<String, String> params = new HashMap<>(6);
      params.put("client_id", clientId);
      params.put("template_name", templateName);
      params.put("subject", subjectStr);
      params.put("from", from);
      params.put("to", to);
      //data: {"content":"正文"}
      Map<String, String> data = Collections.singletonMap("content", contentBuf.toString());
      params.put("data", objectMapper.writeValueAsString(data));

      addInfo("About to send out mail \"" + subjectStr + "\" to " + to);

      sendPostRequest(url, params);
    } catch (Exception e) {
      addError("Error occurred while sending mail.", e);
    }
  }

  /**
   * 向指定 API url 发送 POST 请求
   *
   * @param url    API url
   * @param params 请求参数，需转换为 name1=value1&name2=value2 格式进行发送
   * @return String 响应结果
   */
  private String sendPostRequest(String url, Map<String, String> params) throws IOException {
    PrintWriter out = null;
    BufferedReader in = null;
    StringBuffer resultBuf;
    try {
      String param = buildRequestParam(params);
      HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      conn.setFixedLengthStreamingMode(param.getBytes().length);
      out = new PrintWriter(conn.getOutputStream());
      out.print(param);
      out.flush();
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      resultBuf = new StringBuffer();
      while (true) {
        String line = in.readLine();
        if (line == null) {
          break;
        }
        resultBuf.append(line);
      }
    } finally {
      if (out != null) {
        out.close();
      }
      if (in != null) {
        in.close();
      }
    }
    return resultBuf.toString();
  }

  /**
   * 使用 map 构建请求参数，格式为 name1=value1&name2=value2
   *
   * @param params
   * @return String
   * @throws UnsupportedEncodingException
   */
  private String buildRequestParam(Map<String, String> params) throws UnsupportedEncodingException {
    StringBuffer strBuf = new StringBuffer();
    if (params == null || params.size() == 0) {
      return strBuf.toString();
    }
    for (Map.Entry<String, String> entry : params.entrySet()) {
      String value = entry.getValue();
      String key = entry.getKey();
      boolean invalid = isEmptyString(key) || isEmptyString(value);
      if (invalid) {
        continue;
      }
      //key 和 value 需进行编码，排除 & 字符的干扰
      strBuf.append(URLEncoder.encode(key, charset)).append("=")
          .append(URLEncoder.encode(value, charset)).append("&");
    }
    //移除最后一个 & 符号
    int length = strBuf.length();
    if (length > 0) {
      strBuf = strBuf.delete(length - 1, length);
    }
    return strBuf.toString();
  }

  //Getter & Setter
  //--------------------------------------------------------------------------

  public Layout<ILoggingEvent> getContentLayout() {
    return contentLayout;
  }

  public void setContentLayout(Layout<ILoggingEvent> contentLayout) {
    this.contentLayout = contentLayout;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public boolean isAsyncSend() {
    return asyncSend;
  }

  public void setAsyncSend(boolean asyncSend) {
    this.asyncSend = asyncSend;
  }

  public String getCharset() {
    return charset;
  }

  public void setCharset(String charset) {
    this.charset = charset;
  }

  //内部类
  //--------------------------------------------------------------------------

  /**
   * 用于异步发送邮件
   *
   * @author 小柯
   * @date 2017/12/1
   */
  class SenderRunnable implements Runnable {

    final CyclicBuffer<ILoggingEvent> cyclicBuf;
    final ILoggingEvent event;

    SenderRunnable(CyclicBuffer<ILoggingEvent> cyclicBuf, ILoggingEvent event) {
      this.cyclicBuf = cyclicBuf;
      this.event = event;
    }

    @Override
    public void run() {
      sendMail(cyclicBuf, event);
    }
  }

  //工具方法
  //--------------------------------------------------------------------------

  /**
   * 判断字符串是否为空
   *
   * @param string
   * @return boolean
   */
  private static boolean isEmptyString(String string) {
    return string == null || string.length() == 0;
  }

}
