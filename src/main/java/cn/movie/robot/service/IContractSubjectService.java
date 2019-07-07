package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import org.springframework.data.domain.Pageable;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface IContractSubjectService {
  /**
   * 查询所有合同主体-分页
   * @param pageable
   * @return
   */
  Result queryAll(Pageable pageable);

  /**
   * 查询所有合同主体
   * @return
   */
  Result queryAll();

  /**
   * 更新状态
   * @param contractId
   * @return
   */
  Result updateState(Integer contractId, int state);

  /**
   * 保存合同主体
   * @param name
   * @return
   */
  Result save(String name);

  /**
   * 更新合同主体
   * @param contractId
   * @param name
   * @return
   */
  Result update(Integer contractId, String name);

}
