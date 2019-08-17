package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.project.ProjectBaseInfoVo;
import cn.movie.robot.vo.req.search.BaseSearchVo;
import org.springframework.data.domain.Pageable;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface IProjectService {

  Result queryAll(BaseSearchVo baseSearchVo);
  /**
   * 创建项目
   * @param name
   * @return
   */
  Result create(String name);

  /**
   * 保存项目基本信息
   * @param projectId
   * @param projectBaseInfoVo
   * @return
   */
  Result saveBaseInfo(int projectId, ProjectBaseInfoVo projectBaseInfoVo);

  /**
   * 详情
   * @param projectId
   * @return
   */
  Result detail(int projectId);

  /**
   * 更新项目状态
   * @param projectId
   * @param state
   * @return
   */
  Result updateState(int projectId, Integer state);

}
