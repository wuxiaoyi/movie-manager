package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.project.ProjectBaseInfoVo;
import cn.movie.robot.vo.req.project.ProjectLastStateInfoVo;
import cn.movie.robot.vo.req.project.ProjectShottingInfoVo;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
public interface IProjectService {
  Result create(String name);

  Result saveBaseInfo(int projectId, ProjectBaseInfoVo projectBaseInfoVo);

  Result saveShottingInfo(int projectId, ProjectShottingInfoVo projectShottingInfoVo);

  Result saveLastStateInfo(int projectId, ProjectLastStateInfoVo projectLastStateInfoVo);

}
