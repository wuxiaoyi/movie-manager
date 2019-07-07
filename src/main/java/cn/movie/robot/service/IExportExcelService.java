package cn.movie.robot.service;

import java.io.InputStream;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/7
 */
public interface IExportExcelService {
  InputStream exportProjects(List<Integer> projectIds);
}
