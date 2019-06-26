package cn.movie.robot.controller;

import cn.movie.robot.service.ISessionService;
import cn.movie.robot.vo.common.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
@RestController
@RequestMapping(value = "/session/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SessionController {

  @Autowired
  ISessionService sessionService;

  @PostMapping("login")
  public Result login(@RequestParam("email") String email, @RequestParam("password") String password) {
    return sessionService.login(email, password);
  }

  @DeleteMapping("logout")
  public Result logout(){
    return Result.error("logout");
  }

  @GetMapping("test")
  public Result test(){
    return Result.error("test");
  }
}
