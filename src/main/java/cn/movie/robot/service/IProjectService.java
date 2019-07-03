package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.project.ProjectBaseInfoVo;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface IProjectService {
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

}
