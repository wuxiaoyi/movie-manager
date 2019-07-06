package cn.movie.robot.service;

import cn.movie.robot.model.ProjectDetail;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.project.ProjectFeeDetailVo;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface IProjectDetailService {

  /**
   * 保存项目拍摄费用明细
   * @param projectId
   * @param projectDetails
   * @return
   */
  Result saveShottingInfo(int projectId, List<ProjectFeeDetailVo> projectDetails);

  /**
   * 保存项目后期费用明细
   * @param projectId
   * @param projectDetails
   * @return
   */

  Result saveLastStateInfo(int projectId, List<ProjectFeeDetailVo> projectDetails);


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
