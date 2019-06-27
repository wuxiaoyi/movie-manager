package cn.movie.robot.dao;

import cn.movie.robot.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
  List<UserRole> findAllByUserId(Integer userId);

  @Modifying
  Integer deleteByRoleId(Integer roleId);

  @Modifying
  Integer deleteByUserIdAndRoleIdIn(Integer userId, List<Integer> roleIds);
}
