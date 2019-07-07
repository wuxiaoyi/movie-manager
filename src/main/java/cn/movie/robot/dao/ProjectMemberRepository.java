package cn.movie.robot.dao;

import cn.movie.robot.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Integer> {

  List<ProjectMember> queryByProjectId(int projectId);

  List<ProjectMember> queryByProjectIdIn(List<Integer> projectIds);

  ProjectMember findByProjectIdAndStaffIdAndMemberType(int projectId, int staffId, int memberType);

  Integer deleteByIdIn(List<Integer> ids);

  List<ProjectMember> queryByMemberTypeAndStaffIdIn(int type, List<Integer> staffIds);
}
