package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.search.ProjectSearchVo;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
public interface IProjectSearchService {
  Result search(ProjectSearchVo projectSearchVo);
}
