package cn.movie.robot.dao;

import cn.movie.robot.model.Project;
import cn.movie.robot.model.ProjectDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Integer> {
  List<ProjectDetail> queryByProjectId(int projectId);

  Integer deleteByIdIn(List<Integer> id);

  List<ProjectDetail> queryByProjectIdInAndFeeChildCategoryIdIn(List<Integer> projectIds, List<Integer> feeCategoryIds);

  List<ProjectDetail> queryByProjectIdInAndFeeCategoryIdIsNull(List<Integer> projectIds);
  /**
   * 根据projectid和stage查询二级费用类别不为空的数据
   * @param projectId
   * @param stage
   * @return
   */
  List<ProjectDetail> queryByProjectIdAndStageAndFeeChildCategoryIdIsNotNull(int projectId, int stage);

  /**
   * 根据一级费用项和实际发生金额查询二级费用类别为空的数据
   * @param feeCategoryIds
   * @param amount
   * @return
   */
  List<ProjectDetail> queryByFeeCategoryIdInAndRealAmountGreaterThanAndFeeChildCategoryIdIsNull(List<Integer> feeCategoryIds, BigDecimal amount);

  /**
   * 根据二级费用项和实际发生金额查询数据
   * @param feeCategoryIds
   * @param amount
   * @return
   */
  List<ProjectDetail> queryByFeeChildCategoryIdInAndRealAmountGreaterThan(List<Integer> feeCategoryIds, BigDecimal amount);

  /**
   * 查询一级费用项总额的数据
   * @param feeCategoryIds
   * @return
   */
  List<ProjectDetail> queryByProjectIdInAndFeeCategoryIdInAndFeeChildCategoryIdIsNull(List<Integer> projectIds, List<Integer> feeCategoryIds);
}
