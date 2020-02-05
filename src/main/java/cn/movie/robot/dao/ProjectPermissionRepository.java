package cn.movie.robot.dao;

import cn.movie.robot.model.ProjectPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2020/2/4
 */
public interface ProjectPermissionRepository  extends JpaRepository<ProjectPermission, Integer> {
  List<ProjectPermission> findByProjectIdAndAndPermissionType(Integer projectId, Integer permissionType);

  List<ProjectPermission> findByUserIdAndPermissionType(Integer userId, Integer permissionType);

  ProjectPermission findFirstByUserIdAndProjectIdAndPermissionType(Integer userId, Integer projectId, Integer permissionType);

  @Modifying
  Integer deleteByProjectIdAndPermissionTypeAndUserIdIn(Integer ProjectId, Integer permissionType, List<Integer> userIds);
}
