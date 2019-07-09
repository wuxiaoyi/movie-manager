package cn.movie.robot.dao;

import cn.movie.robot.model.CustomerCompany;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Wuxiaoyi
 * @date 2019/7/9
 */
public interface CustomerCompanyRepository extends JpaRepository<CustomerCompany, Integer> {
}
