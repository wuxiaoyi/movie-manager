package cn.movie.robot.dao;

import cn.movie.robot.model.Provider;
import cn.movie.robot.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface ProviderRepository extends JpaRepository<Provider, Integer>, JpaSpecificationExecutor<Provider> {
  List<Provider> queryByState(Integer state);
  List<Provider> queryByIdIn(List<Integer> ids);
  Provider findByName(String name);
}
