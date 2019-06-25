package cn.movie.robot.controller;

import cn.movie.robot.vo.common.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
@RestController
@RequestMapping(value = "/session/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SessionController {

  @GetMapping("login")
  public Result login() {
    return Result.error("login");
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
