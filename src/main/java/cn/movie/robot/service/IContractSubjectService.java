package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import org.springframework.data.domain.Pageable;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface IContractSubjectService {
  /**
   * 查询所有合同主体
   * @param pageable
   * @return
   */
  Result queryAll(Pageable pageable);

  /**
   * 查询生效的合同主体
   * @return
   */
  Result queryNormal();

  /**
   * 禁用合同主体
   * @param contractId
   * @return
   */
  Result forbiddenContractSubject(Integer contractId);

  /**
   * 保存合同主体
   * @param name
   * @return
   */
  Result save(String name);

}
