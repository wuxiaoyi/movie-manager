package cn.movie.robot.handler;

import cn.movie.robot.common.Constants;
import cn.movie.robot.vo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
      AuthorizationException.class
  })
  public Object handleAuthorizationException(Exception e, HttpServletRequest request){
    return new Result(Constants.NO_PERMISSION_ERROR_CODE, Constants.NO_PERMISSION_ERROR_MSG);
  }

  @ExceptionHandler({
      ConstraintViolationException.class
  })
  public Object handleConstraintViolationException(Exception e, HttpServletRequest request){
    return Result.error(e.getLocalizedMessage());
  }

  @ExceptionHandler(Exception.class)
  public Object handleErrorException(Exception e, HttpServletRequest request){
    String method = StringUtils.EMPTY;
    String url = StringUtils.EMPTY;
    if (request != null) {
      method = request.getMethod();
      url = request.getRequestURL().toString();
    }
    logger.error("Error occurred, method: {}, url: {}.", method, url, e);
    return new Result(Constants.UNKNOW_EXCEPTION_ERROR_CODE, Constants.UNKNOW_EXCEPTION_ERROR_MSG);
  }
}
