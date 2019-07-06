package cn.movie.robot.controller;

import cn.movie.robot.common.Constants;
import cn.movie.robot.service.IRoleService;
import cn.movie.robot.service.IUserService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ResetPwdVo;
import cn.movie.robot.vo.req.SignUpVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;

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

  @GetMapping("")
  public Result list(@RequestParam("page") int page, @RequestParam("page_size") int pageSize){
    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));
    return userService.queryAll(pageable);
  }

  @PutMapping("/{user_id}/update_role")
  public Result updatePermission(@PathVariable("user_id") Integer userId, @RequestBody List<Integer> roleIdList){
    return roleService.updateUser(userId, roleIdList);
  }

  @GetMapping("/signup_key")
  public Result getSignUpKey(){
    return userService.signUpKey();
  }

  @PutMapping("/{user_id}/reset_pwd")
  public Result resetPwd(@PathVariable("user_id") Integer userId, @RequestBody ResetPwdVo resetPwdVo){
    return userService.resetPwd(userId, resetPwdVo.getPassword());
  }

  @PostMapping("")
  public Result signUp(@RequestBody SignUpVo signUpVo){
    return userService.signUp(signUpVo);
  }

  @DeleteMapping("/{user_id}")
  public Result forbidden(@PathVariable("user_id") Integer userId){
    return userService.updateState(userId, Constants.COMMON_STATE_FORBIDDEN);
  }

  @PutMapping("/{user_id}/recover")
  public Result recover(@PathVariable("user_id") Integer userId){
    return userService.updateState(userId, Constants.COMMON_STATE_NORMAL);
  }

  @GetMapping("/{user_id}/roles")
  public Result userRoles(@PathVariable("user_id") Integer userId){
    return roleService.queryByUserId(userId);
  }

}
