package cn.movie.robot.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/7
 */
public interface IExportExcelService {
  XSSFWorkbook exportProjects(List<Integer> projectIds);
}
