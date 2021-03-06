package cn.movie.robot.dao;

import cn.movie.robot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
public interface UserRepository extends JpaRepository<User, Integer> {
  User findByEmail(String email);

  User findByCellphone(String cellphone);

  User findByEmailOrCellphone(String email, String cellphone);

  List<User> findByIdIn(List<Integer> ids);
}
