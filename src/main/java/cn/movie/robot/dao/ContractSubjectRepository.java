package cn.movie.robot.dao;

import cn.movie.robot.model.ContractSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface ContractSubjectRepository extends JpaRepository<ContractSubject, Integer> {
  List<ContractSubject> queryByState(Integer state);
}
