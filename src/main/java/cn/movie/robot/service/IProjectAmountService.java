package cn.movie.robot.service;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
public interface IProjectAmountService {
  /**
   * 根据费用明细刷新总额
   * @param projectId
   */
  void refreshAmount(Integer projectId);
}
