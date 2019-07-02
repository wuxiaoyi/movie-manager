package cn.movie.robot.dao;

import cn.movie.robot.model.FeeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface FeeCategoryRepository extends JpaRepository<FeeCategory, Integer> {
  List<FeeCategory> queryByStageAndStateAndCategoryType(Integer stage, Integer state, Integer type);
}
