package cn.movie.robot.dao;

import cn.movie.robot.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/1
 */
public interface StaffRepository extends JpaRepository<Staff, Integer> {
  List<Staff> queryByState(Integer state);
  List<Staff> queryByStateAndAscription(Integer state, Integer ascription);
}
