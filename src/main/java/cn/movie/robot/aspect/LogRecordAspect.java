package cn.movie.robot.aspect;


import cn.movie.robot.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志切面
 *
 * @author tw
 * @author 小柯
 * @date 2017/6/22
 */
@Aspect
@Order(1)
@Component
@Slf4j
public class LogRecordAspect {

  @Pointcut("execution(public * cn.movie.robot.controller..*(..))")
  public void aspect() {
  }

  @Around("aspect()")
  public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String ip = this.getRemoteIp(request);
    String uri = request.getRequestURI();
    String method = request.getMethod();
    String params = this.getParamString(joinPoint.getArgs());
    Object result = null;
    try {
      result = joinPoint.proceed();
      return result;
    } finally {
      logger.info("IP:{}, URI:{}, Method:{}, Param:{}, Result:{}, Cost:{}ms",
          ip, uri, method, params, this.convertObjectToJson(result), System.currentTimeMillis() - start);
    }
  }

  private String getParamString(Object[] args) {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    for (int i = 0; i < args.length; i++) {
      Object arg = args[i];
      if (arg instanceof String || arg instanceof Number) {
        builder.append(arg);
      } else {
        builder.append(this.convertObjectToJson(arg));
      }
      if (i < args.length - 1) {
        builder.append(", ");
      }
    }
    builder.append("]");
    return builder.toString().replaceAll("\n", "");
  }

  private String convertObjectToJson(Object object) {
    if (object == null) {
      return "null";
    }
    if (object instanceof BindingResult) {
      return object.toString();
    }
    return JacksonUtil.writeValue(object);
  }

  private String getRemoteIp(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("ProxyConfig-Client-IP");
    }
    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-ProxyConfig-Client-IP");
    }
    if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

}
