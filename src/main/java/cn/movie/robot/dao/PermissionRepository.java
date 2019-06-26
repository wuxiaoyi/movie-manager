package cn.movie.robot.dao;

import cn.movie.robot.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
  List<Permission> findAllByIdIn(List<Integer> ids);
}
