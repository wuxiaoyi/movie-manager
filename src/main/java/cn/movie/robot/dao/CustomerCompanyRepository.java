package cn.movie.robot.dao;

import cn.movie.robot.model.CustomerCompany;
import cn.movie.robot.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Wuxiaoyi
 * @date 2019/7/9
 */
public interface CustomerCompanyRepository extends JpaRepository<CustomerCompany, Integer>, JpaSpecificationExecutor<CustomerCompany> {
  CustomerCompany findByName(String name);
}
