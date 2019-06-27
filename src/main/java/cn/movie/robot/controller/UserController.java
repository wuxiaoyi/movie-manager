package cn.movie.robot.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
@RestController
@RequestMapping(value = "/api/v1/users/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {
}
