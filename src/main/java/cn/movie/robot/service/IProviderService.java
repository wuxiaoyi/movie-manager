package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ProviderVo;
import org.springframework.data.domain.Pageable;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface IProviderService {
  /**
   * 查询所有供应商
   * @param pageable
   * @return
   */
  Result queryAll(Pageable pageable);

  /**
   * 根据状态查询供应商
   * @return
   */
  Result queryNormal();

  /**
   * 禁用供应商
   * @param providerId
   * @return
   */
  Result forbiddenProvider(Integer providerId);

  /**
   * 创建员工
   * @param name
   * @return
   */
  Result save(ProviderVo providerVo);
}
