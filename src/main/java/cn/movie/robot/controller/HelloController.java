package cn.movie.robot.controller;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wuxiaoyi
 * @date 2019/6/14
 */
@RestController
@RequestMapping(value = "/api/v1/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HelloController {

  @Autowired
  TestService testService;
  @GetMapping("hello")
  public Result listUpstreamNodes() {
    return Result.error("demo1 hello");
  }
}
