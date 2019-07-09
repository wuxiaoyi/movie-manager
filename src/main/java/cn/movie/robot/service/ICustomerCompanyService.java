package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.CustomerCompanyVo;
import org.springframework.data.domain.Pageable;

/**
 * @author Wuxiaoyi
 * @date 2019/7/9
 */
public interface ICustomerCompanyService {
  Result queryAll(Pageable pageable);

  /**
   * 查询所有合同主体
   * @return
   */
  Result queryAll();

  /**
   * 更新状态
   * @param id
   * @return
   */
  Result updateState(Integer id, int state);

  /**
   * 保存合同主体
   * @param customerCompanyVo
   * @return
   */
  Result save(CustomerCompanyVo customerCompanyVo);

  /**
   * 更新客户公司
   * @param id
   * @param customerCompanyVo
   * @return
   */
  Result update(Integer id, CustomerCompanyVo customerCompanyVo);
}
