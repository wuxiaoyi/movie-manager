package cn.movie.robot.dao;

import cn.movie.robot.model.ProjectDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Integer> {
  List<ProjectDetail> queryByProjectIdAndStage(int projectId, int stage);
  Integer deleteByIdIn(List<Integer> id);
}
