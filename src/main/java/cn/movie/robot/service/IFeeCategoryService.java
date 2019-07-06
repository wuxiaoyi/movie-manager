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
  Result queryByTypeAndState(int type, int state);

  /**
   * 修改状态
   * @param id
   * @return
   */
  Result updateState(Integer id, int state);

  /**
   * 保存
   * @param feeCategoryVo
   * @return
   */
  Result save(FeeCategoryVo feeCategoryVo);

  /**
   * 更新
   * @param id
   * @param feeCategoryVo
   * @return
   */
  Result update(Integer id, FeeCategoryVo feeCategoryVo);
}
