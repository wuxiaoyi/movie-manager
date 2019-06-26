package cn.movie.robot.dao;

import cn.movie.robot.model.PermissionRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
public interface PermissionRoleRepository extends JpaRepository<PermissionRole, Integer> {
  List<PermissionRole> findAllByRoleIdIn(List<Integer> roleIds);
}
