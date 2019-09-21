package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ProviderSearchVo;
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
   * 搜索供应商
   * @param providerSearchVo
   * @return
   */
  Result search(ProviderSearchVo providerSearchVo);

  /**
   * 根据状态查询供应商
   * @return
   */
  Result queryAll();

  /**
   * 禁用供应商
   * @param providerId
   * @return
   */
  Result updateState(Integer providerId, int state);

  /**
   * 创建供应商
   * @param providerVo
   * @return
   */
  Result save(ProviderVo providerVo);

  /**
   * 更新供应商
   * @param providerId
   * @param providerVo
   * @return
   */
  Result update(Integer providerId, ProviderVo providerVo);
}
