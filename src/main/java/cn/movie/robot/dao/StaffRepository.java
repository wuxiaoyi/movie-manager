package cn.movie.robot.dao;

import cn.movie.robot.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/1
 */
public interface StaffRepository extends JpaRepository<Staff, Integer>, JpaSpecificationExecutor<Staff> {
  List<Staff> queryByState(Integer state);
  List<Staff> queryByAscription(Integer ascription);
  List<Staff> queryByIdIn(List<Integer> ids);
}
