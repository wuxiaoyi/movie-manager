package cn.movie.robot.controller;

import cn.movie.robot.service.IExportExcelService;
import cn.movie.robot.service.IProjectSearchService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.search.ProjectSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@RestController
@RequestMapping(value = "/api/v1/project_search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProjectSearchController {

  @Autowired
  IProjectSearchService projectSearchService;

  @Autowired
  IExportExcelService exportExcelService;

  @PostMapping("")
  public Result search(@RequestBody ProjectSearchVo projectSearchVo){
    return projectSearchService.search(projectSearchVo);
  }

  @PostMapping("/export")
  public Result export(){
    List<Integer> pids = new ArrayList<>();
    pids.add(1);
    exportExcelService.exportProjects(pids);
    return Result.succ();
  }

}
