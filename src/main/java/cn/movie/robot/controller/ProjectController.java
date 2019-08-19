package cn.movie.robot.controller;

import cn.movie.robot.service.IExportExcelService;
import cn.movie.robot.service.IOplogService;
import cn.movie.robot.service.IProjectDetailService;
import cn.movie.robot.service.IProjectService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.oplog.ProjectBaseInfoOplog;
import cn.movie.robot.vo.oplog.ProjectLastStateDetailOplog;
import cn.movie.robot.vo.oplog.ProjectShootingDetailOplog;
import cn.movie.robot.vo.req.ProjectStateVo;
import cn.movie.robot.vo.req.project.*;
import cn.movie.robot.vo.req.search.BaseSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/projects", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProjectController {

  @Autowired
  IProjectService projectService;

  @Autowired
  IProjectDetailService projectDetailService;

  @Autowired
  IOplogService oplogService;

  @Autowired
  IExportExcelService exportExcelService;

  @PostMapping("")
  public Result create(@RequestBody ProjectCreateVo projectCreateVo){
    return projectService.create(projectCreateVo.getName());
  }

  @GetMapping("")
  public Result list(@RequestBody BaseSearchVo baseSearchVo){
    return projectService.queryAll(baseSearchVo);
  }

  @GetMapping("/{id}/base_info")
  public Result baseInfo(@PathVariable("id") Integer id){
    return projectService.detail(id);
  }

  @GetMapping("/{id}/shooting_info")
  public Result shootingInfo(@PathVariable("id") Integer id){
    return projectDetailService.shootingDetail(id);
  }

  @GetMapping("/{id}/last_state_info")
  public Result lastStateInfo(@PathVariable("id") Integer id){
    return projectDetailService.lastStateDetail(id);
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

  @PutMapping("/{id}/update_state")
  public Result updateState(@PathVariable("id") Integer id, @RequestBody ProjectStateVo projectStateVo){
    return projectService.updateState(id, projectStateVo.getState());
  }

  @GetMapping("/{id}/export_detail")
  public void exportDetail(@PathVariable("id") Integer id){
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletResponse response = servletRequestAttributes.getResponse();

    XSSFWorkbook excel = exportExcelService.exportDetail(id);

    try {
      response.reset();
      OutputStream os = new BufferedOutputStream(response.getOutputStream());
      response.addHeader("Content-Disposition", "attachment;filename=projects.xlsx");
      response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
      excel.write(os);
      os.flush();
      os.close();
    } catch (Exception e) {
      logger.error("ProjectSearchController export error, e: {}", e);
    }
  }
}
