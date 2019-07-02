package cn.movie.robot.dao;

import cn.movie.robot.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface ProviderRepository extends JpaRepository<Provider, Integer> {
  List<Provider> queryByState(Integer state);
}
