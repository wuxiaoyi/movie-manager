package cn.movie.robot.service.impl;

import cn.movie.robot.model.Permission;
import cn.movie.robot.model.User;
import cn.movie.robot.service.IPermissionService;
import cn.movie.robot.service.ISessionService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.resp.LoginVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
@Service
public class SessionServiceImpl implements ISessionService {

  @Autowired
  IPermissionService permissionService;

  @Override
  public Result login(String email, String password) { Result result = new Result();
    try {
      Subject subject = SecurityUtils.getSubject();
      UsernamePasswordToken token = new UsernamePasswordToken(email, password);
      subject.login(token);

      LoginVo loginVo = new LoginVo();
      String authorization = (String) subject.getSession().getId();
      loginVo.setAuthCode(authorization);

      User user = (User)subject.getPrincipal();
      List<Permission> permissions = permissionService.queryPermissionByUser(user);
      loginVo.setPermissions(permissions.stream().map(Permission::getName).collect(Collectors.toList()));

      return Result.succ(loginVo);
    } catch (IncorrectCredentialsException e) {
      return Result.error("密码错误");
    } catch (LockedAccountException e) {
      return Result.error("该用户已被禁用");
    } catch (AuthenticationException e) {
      return Result.error("该用户不存在");
    }
  }
}
