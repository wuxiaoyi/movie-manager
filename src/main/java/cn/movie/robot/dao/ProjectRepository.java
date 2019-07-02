package cn.movie.robot.dao;

import cn.movie.robot.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {
  Project findByName(String name);
}
