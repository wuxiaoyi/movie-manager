package cn.movie.robot.controller;

import cn.movie.robot.service.IExportExcelService;
import cn.movie.robot.service.IProjectSearchService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.search.ProjectSearchVo;
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

  @GetMapping("/export")
  public void export(){
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletResponse response = servletRequestAttributes.getResponse();

    List<Integer> pids = new ArrayList<>();
    pids.add(1);
    XSSFWorkbook excel = exportExcelService.exportProjects(pids);

    try {
      //清空response
      response.reset();
      //设置response的Header
      response.addHeader("Content-Disposition", "attachment;filename=tete.xlsx");
      OutputStream os = new BufferedOutputStream(response.getOutputStream());
      response.setContentType("application/vnd.ms-excel;charset=gb2312");
      //将excel写入到输出流中
      excel.write(os);
      os.flush();
      os.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
