package cn.movie.robot.service;

import cn.movie.robot.model.ProjectDetail;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.project.ProjectLastStateInfoVo;
import cn.movie.robot.vo.req.project.ProjectShottingInfoVo;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface IProjectDetailService {

  /**
   * 保存项目拍摄费用明细
   * @param projectId
   * @param projectShottingInfoVo
   * @return
   */
  Result saveShottingInfo(int projectId, ProjectShottingInfoVo projectShottingInfoVo);

  /**
   * 保存项目后期费用明细
   * @param projectId
   * @param projectLastStateInfoVo
   * @return
   */

  Result saveLastStateInfo(int projectId, ProjectLastStateInfoVo projectLastStateInfoVo);


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
