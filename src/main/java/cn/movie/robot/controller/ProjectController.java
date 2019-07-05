package cn.movie.robot.controller;

import cn.movie.robot.service.IOplogService;
import cn.movie.robot.service.IProjectService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.oplog.ProjectBaseInfoOplog;
import cn.movie.robot.vo.req.project.ProjectBaseInfoVo;
import cn.movie.robot.vo.req.project.ProjectLastStateInfoVo;
import cn.movie.robot.vo.req.project.ProjectShottingInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@RestController
@RequestMapping(value = "/api/v1/projects", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProjectController {

  @Autowired
  IProjectService projectService;

  @Autowired
  IOplogService oplogService;

  @PostMapping("")
  public Result create(@RequestParam("name") String name){
    return projectService.create(name);
  }

  @GetMapping("/{id}/base_info")
  public Result baseInfo(@PathVariable("id") Integer id){
    return Result.succ();
  }

  @GetMapping("/{id}/shooting_info")
  public Result shootingInfo(@PathVariable("id") Integer id){
    return Result.succ();
  }

  @GetMapping("/{id}/last_state_info")
  public Result lastStateInfo(@PathVariable("id") Integer id){
    return Result.succ();
  }

  @PutMapping("/{id}/base_info")
  public Result updateBaseInfo(@PathVariable("id") Integer id, @RequestBody ProjectBaseInfoVo projectBaseInfoVo){
    ProjectBaseInfoOplog oldInfo = oplogService.buildBaseInfoOplog(id);
    Result result = projectService.saveBaseInfo(id, projectBaseInfoVo);
    ProjectBaseInfoOplog newInfo = oplogService.buildBaseInfoOplog(id);
    oplogService.saveBaseInfoOplog(newInfo, oldInfo);
    return result;
  }

  @PutMapping("/{id}/shooting_info")
  public Result updateShootingInfo(@PathVariable("id") Integer id, @RequestBody ProjectShottingInfoVo projectShottingInfoVo){
    return Result.succ();
  }

  @PutMapping("/{id}/last_state_info")
  public Result updateLastStateInfo(@PathVariable("id") Integer id, @RequestBody ProjectLastStateInfoVo projectLastStateInfoVo){
    return Result.succ();
  }
}
