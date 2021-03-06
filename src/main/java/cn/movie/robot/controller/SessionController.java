package cn.movie.robot.controller;

import cn.movie.robot.common.Constants;
import cn.movie.robot.model.User;
import cn.movie.robot.service.ISessionService;
import cn.movie.robot.service.IUserService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ResetPwdVo;
import cn.movie.robot.vo.req.SignInVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
@RestController
@RequestMapping(value = "/api/v1/session/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SessionController {

  @Autowired
  ISessionService sessionService;

  @Autowired
  IUserService userService;

  @PostMapping("login")
  public Result login(@RequestBody SignInVo signInVo) {
    return sessionService.login(signInVo.getEmail(), signInVo.getPassword());
  }

  @DeleteMapping("logout")
  public Result logout(){
    Subject subject = SecurityUtils.getSubject();
    subject.logout();
    return Result.succ();
  }

  @GetMapping("unauth")
  public Result unauth(){
    return new Result(Constants.NO_AUTH_ERROR_CODE, Constants.NO_AUTH_ERROR_MSG);
  }

  @PutMapping("reset_pwd")
  public Result resetPwd(@RequestBody ResetPwdVo resetPwdVo){
    Subject subject = SecurityUtils.getSubject();
    User user = (User)subject.getPrincipal();
    return userService.resetPwd(user.getId(), resetPwdVo.getPassword());
  }

  @RequiresPermissions("user:view")
  @GetMapping("test")
  public Result test(){
    return Result.error("test1");
  }

  @RequiresPermissions("user:manage")
  @GetMapping("test2")
  public Result test2(){
    return Result.error("test2");
  }
}
