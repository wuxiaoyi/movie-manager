package cn.movie.robot.service.impl;

import cn.movie.robot.service.ISessionService;
import cn.movie.robot.vo.common.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
@Service
public class SessionServiceImpl implements ISessionService {
  @Override
  public Result login(String email, String password) {
    Result result = new Result();
    try {
      Subject subject = SecurityUtils.getSubject();
      UsernamePasswordToken token = new UsernamePasswordToken(email, password);
      subject.login(token);
      String authorization = (String) subject.getSession().getId();
      result.setData(authorization);
    } catch (IncorrectCredentialsException e) {
      return Result.error("密码错误");
    } catch (LockedAccountException e) {
      return Result.error("该用户已被禁用");
    } catch (AuthenticationException e) {
      return Result.error("该用户不存在");
    }

    return result;
  }
}
