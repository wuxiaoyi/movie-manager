package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.FeeCategoryVo;
import org.springframework.data.domain.Pageable;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
public interface IFeeCategoryService {
  /**
   * 查询所有费用类别
   * @param pageable
   * @return
   */
  Result queryAll(Pageable pageable);

  /**
   * 查询生效的费用类别
   * @return
   */
  Result queryNormal();

  /**
   * 禁用
   * @param id
   * @return
   */
  Result forbidden(Integer id);

  /**
   * 保存
   * @param feeCategoryVo
   * @return
   */
  Result save(FeeCategoryVo feeCategoryVo);
}
