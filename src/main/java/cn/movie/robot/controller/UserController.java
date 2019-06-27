package cn.movie.robot.controller;

import cn.movie.robot.service.IRoleService;
import cn.movie.robot.vo.common.Result;
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
@RequestMapping(value = "/api/v1/users/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

  @Autowired
  IRoleService roleService;

  @PutMapping("/{user_id}/update_role")
  public Result updatePermission(@PathVariable("user_id") Integer userId, @RequestParam("role_ids") String roldIds){
    List<Integer> roleIdList = Arrays.asList(roldIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
    return roleService.updateUser(userId, roleIdList);
  }

}
