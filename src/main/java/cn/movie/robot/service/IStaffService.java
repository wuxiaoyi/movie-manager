package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.StaffSearchVo;
import cn.movie.robot.vo.req.StaffVo;
import org.springframework.data.domain.Pageable;

/**
 * @author Wuxiaoyi
 * @date 2019/7/1
 */
public interface IStaffService {
  /**
   * 查询所有员工
   * @param pageable
   * @return
   */
  Result queryAll(Pageable pageable);

  /**
   * 搜索员工
   * @param staffSearchVo
   * @return
   */
  Result search(StaffSearchVo staffSearchVo);

  /**
   * 查询所有员工
   * @return
   */
  Result queryAll();

  /**
   * 更新状态
   * @param staffId
   * @return
   */
  Result updateState(Integer staffId, int state);

  /**
   * 创建员工
   * @param staffVo
   * @return
   */
  Result save(StaffVo staffVo);

  /**
   * 更新员工
   * @param staffId
   * @param staffVo
   * @return
   */
  Result update(Integer staffId, StaffVo staffVo);
}
