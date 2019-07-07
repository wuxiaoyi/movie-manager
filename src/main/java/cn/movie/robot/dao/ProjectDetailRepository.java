package cn.movie.robot.dao;

import cn.movie.robot.model.ProjectDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Integer> {
  List<ProjectDetail> queryByProjectIdAndStageAndFeeChildCategoryIdIsNotNull(int projectId, int stage);
  List<ProjectDetail> queryByProjectId(int projectId);
  Integer deleteByIdIn(List<Integer> id);
  List<ProjectDetail> queryByFeeCategoryIdInAndRealAmountGreaterThanAndFeeChildCategoryIdIsNull(List<Integer> feeCategoryIds, BigDecimal amount);
  List<ProjectDetail> queryByFeeChildCategoryIdInAndRealAmountGreaterThan(List<Integer> feeCategoryIds, BigDecimal amount);
}
