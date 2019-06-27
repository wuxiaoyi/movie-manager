package cn.movie.robot.dao;

import cn.movie.robot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
  List<Role> findAllByIdIn(List<Integer> ids);

  Role findByName(String name);
}
