package cn.movie.robot.controller;

import cn.movie.robot.service.IExportExcelService;
import cn.movie.robot.service.IProjectSearchService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.search.ProjectSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Slf4j
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
  public void export(@RequestBody ProjectSearchVo projectSearchVo){
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletResponse response = servletRequestAttributes.getResponse();

    List<Integer> projectIds = projectSearchService.searchForExport(projectSearchVo);

    if (projectIds.size() == 0 ){
      return;
    }

    XSSFWorkbook excel = exportExcelService.exportProjects(projectIds);

    try {
      //清空response
      response.reset();
      //设置response的Header
      OutputStream os = new BufferedOutputStream(response.getOutputStream());
      response.addHeader("Content-Disposition", "attachment;filename=projects.xlsx");
      response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
      //将excel写入到输出流中
      excel.write(os);
      os.flush();
      os.close();
    } catch (Exception e) {
      logger.error("ProjectSearchController export error, e: {}", e);
    }
  }

}
