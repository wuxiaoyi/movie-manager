package cn.movie.robot.service;

import cn.movie.robot.model.Staff;
import cn.movie.robot.vo.common.Result;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
   * 根据状态查询员工
   * @return
   */
  Result queryNormal();

  /**
   * 禁用员工
   * @param staffId
   * @return
   */
  Result forbiddenStaff(Integer staffId);

  /**
   * 创建员工
   * @param name
   * @return
   */
  Result save(String name);
}
