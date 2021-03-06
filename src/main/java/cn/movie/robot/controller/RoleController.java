package cn.movie.robot.controller;

import cn.movie.robot.common.Constants;
import cn.movie.robot.service.IRoleService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.RolePermissionVo;
import cn.movie.robot.vo.req.RoleVo;
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
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RoleController {

  @Autowired
  IRoleService roleService;

  @GetMapping("")
  public Result list(@RequestParam("page") int page, @RequestParam("page_size") int pageSize){
    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));
    return roleService.queryAll(pageable);
  }

  @GetMapping("/normal_all")
  public Result normalAll(){
    return roleService.normalAll();
  }

  @PostMapping("")
  public Result save(@RequestBody RoleVo roleVo){
    return roleService.save(roleVo.getName());
  }

  @PostMapping("/{role_id}")
  public Result update(@PathVariable("role_id") Integer roleId, @RequestBody RoleVo roleVo){
    return roleService.update(roleId, roleVo);
  }

  @PutMapping("/{role_id}/update_permission")
  public Result updatePermission(@PathVariable("role_id") Integer roleId, @RequestBody RolePermissionVo rolePermissionVo){
    return roleService.updatePermission(roleId, rolePermissionVo);
  }

  @GetMapping("/{role_id}/permissions")
  public Result rolePermissions(@PathVariable("role_id") Integer roldId){
    return roleService.queryPermissionsByRoleId(roldId);
  }

}
