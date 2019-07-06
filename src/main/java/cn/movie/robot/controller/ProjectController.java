package cn.movie.robot.controller;

import cn.movie.robot.common.Constants;
import cn.movie.robot.service.IOplogService;
import cn.movie.robot.service.IProjectDetailService;
import cn.movie.robot.service.IProjectService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.oplog.ProjectBaseInfoOplog;
import cn.movie.robot.vo.oplog.ProjectLastStateDetailOplog;
import cn.movie.robot.vo.oplog.ProjectShootingDetailOplog;
import cn.movie.robot.vo.req.project.ProjectBaseInfoVo;
import cn.movie.robot.vo.req.project.ProjectFeeDetailVo;
import cn.movie.robot.vo.req.project.ProjectLastStateInfoVo;
import cn.movie.robot.vo.req.project.ProjectShottingInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;

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
  IProjectDetailService projectDetailService;

  @Autowired
  IOplogService oplogService;

  @PostMapping("")
  public Result create(@RequestParam("name") String name){
    return projectService.create(name);
  }

  @GetMapping("")
  public Result list(@RequestParam("page") int page, @RequestParam("page_size") int pageSize){
    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));
    return projectService.queryAll(pageable);
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
  public Result updateShootingInfo(@PathVariable("id") Integer id, @RequestBody List<ProjectFeeDetailVo> projectFeeDetailVoList){
    ProjectShootingDetailOplog oldInfo = oplogService.buildShootingOplog(id);
    Result result = projectDetailService.saveShottingInfo(id, projectFeeDetailVoList);
    ProjectShootingDetailOplog newInfo = oplogService.buildShootingOplog(id);
    oplogService.saveShootingOplog(newInfo, oldInfo);
    return result;
  }

  @PutMapping("/{id}/last_state_info")
  public Result updateLastStateInfo(@PathVariable("id") Integer id, @RequestBody List<ProjectFeeDetailVo> projectFeeDetailVoList){
    ProjectLastStateDetailOplog oldInfo = oplogService.buildLastStateOplog(id);
    Result result = projectDetailService.saveLastStateInfo(id, projectFeeDetailVoList);
    ProjectLastStateDetailOplog newInfo = oplogService.buildLastStateOplog(id);
    oplogService.saveLastStateOplog(newInfo, oldInfo);
    return result;
  }
}
