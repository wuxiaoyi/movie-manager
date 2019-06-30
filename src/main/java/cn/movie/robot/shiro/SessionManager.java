package cn.movie.robot.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
public class SessionManager extends DefaultWebSessionManager {
  private static final String AUTHORIZATION = "X-Token";

  public SessionManager() {
    super();
  }

  @Override
  protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
    //前端ajax的headers中必须传入Authorization的值
    String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
    //如果请求头中有 Authorization 则其值为sessionId
    if (!StringUtils.isEmpty(id)) {
      request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
      request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
      return id;
    } else {
      //否则按默认规则从cookie取sessionId
      return super.getSessionId(request, response);
    }
  }
}
