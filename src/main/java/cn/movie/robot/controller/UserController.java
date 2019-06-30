package cn.movie.robot.controller;

import cn.movie.robot.service.IRoleService;
import cn.movie.robot.service.IUserService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.SignUpVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

  @Autowired
  IRoleService roleService;

  @Autowired
  IUserService userService;

  @PutMapping("/{user_id}/update_role")
  public Result updatePermission(@PathVariable("user_id") Integer userId, @RequestParam("role_ids") String roldIds){
    List<Integer> roleIdList = Arrays.asList(roldIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
    return roleService.updateUser(userId, roleIdList);
  }

  @GetMapping("/signup_key")
  public Result getSignUpKey(){
    return userService.signUpKey();
  }

  @GetMapping("/allow_forget_pwd")
  public Result allowForgetPwd(@RequestParam("email") String email){
    return userService.forgetPwdKey(email);
  }

  @GetMapping("/reset_pwd")
  public Result resetPwd(@RequestParam("email") String email, @RequestParam("password") String password){
    return userService.resetPwd(email, password);
  }

  @PostMapping("/")
  public Result signUp(@RequestBody SignUpVo signUpVo){
    return userService.signUp(signUpVo);
  }

  @DeleteMapping("/{user_id}")
  public Result forbiddenUser(@PathVariable("user_id") Integer userId){
    return userService.forbiddenUser(userId);
  }

}
