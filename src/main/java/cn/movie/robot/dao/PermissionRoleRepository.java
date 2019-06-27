package cn.movie.robot.dao;

import cn.movie.robot.model.PermissionRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
public interface PermissionRoleRepository extends JpaRepository<PermissionRole, Integer> {
  List<PermissionRole> findAllByRoleIdIn(List<Integer> roleIds);

  List<PermissionRole> findAllByRoleId(Integer roleId);

  @Modifying
  Integer deleteByRoleIdAndPermissionIdIn(Integer roleId, List<Integer> permissionIds);

  @Modifying
  Integer deleteByRoleId(Integer roleId);
}
