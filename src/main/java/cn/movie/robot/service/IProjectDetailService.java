package cn.movie.robot.service;

import cn.movie.robot.model.ProjectDetail;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface IProjectDetailService {
  /**
   * 初始化拍摄费用明细
   * @param projectId
   * @return
   */
  List<ProjectDetail> initShootingInfo(Integer projectId);

  /**
   * 初始化后期费用明细
   * @param projectId
   * @return
   */
  List<ProjectDetail> initLastStateInfo(Integer projectId);
}
