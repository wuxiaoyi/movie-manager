package cn.movie.robot.dao;

import cn.movie.robot.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {
}
