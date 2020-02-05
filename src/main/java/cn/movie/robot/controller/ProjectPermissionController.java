package cn.movie.robot.controller;

import cn.movie.robot.service.IProjectPermissionService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ProjectPermissionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wuxiaoyi
 * @date 2020/2/4
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/project_permissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProjectPermissionController {

  @Autowired
  IProjectPermissionService projectPermissionService;

  @GetMapping("/{id}/permissions")
  public Result list(@PathVariable("id") Integer id, @RequestParam("permission_type") Integer permissionType){
    return projectPermissionService.list(id, permissionType);
  }

  @PostMapping("/{id}/permissions")
  public Result update(@PathVariable("id") Integer id, @RequestBody ProjectPermissionVo projectPermissionVo){
    return projectPermissionService.update(id, projectPermissionVo);
  }

}
