package cn.movie.robot.controller;

import cn.movie.robot.dao.ProjectRepository;
import cn.movie.robot.model.Project;
import cn.movie.robot.service.IProjectAmountService;
import cn.movie.robot.service.IProjectDetailService;
import cn.movie.robot.service.IProjectService;
import cn.movie.robot.service.ITaskService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ProjectStateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/9/11
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/tasks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TaskController {

  @Autowired
  ITaskService taskService;

  @Autowired
  IProjectAmountService projectAmountService;

  @Resource
  ProjectRepository projectRepository;

  @PostMapping("/refresh_amount")
  public Result updateState(){
    List<Project> projects = projectRepository.findAll();
    for (Project project : projects){
      taskService.refreshProjectAmount(project.getId());
      projectAmountService.refreshAmount(project.getId());
    }
    return Result.succ();
  }

  @PostMapping("/fix_project_creator")
  public Result fixProjectCreator(){
    List<Project> projects = projectRepository.findAll();
    return Result.succ();
  }
}
