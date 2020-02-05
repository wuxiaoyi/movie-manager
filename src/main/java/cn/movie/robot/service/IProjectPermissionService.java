package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ProjectPermissionVo;
import cn.movie.robot.vo.resp.ProjectPermissionRespVo;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2020/2/4
 */
public interface IProjectPermissionService {
  Result list(Integer projectId, Integer permissionType);

  Result update(Integer projectId, ProjectPermissionVo projectPermissionVo);

  List<Integer> queryUserAvailProjectIds(Integer permissionType);
}
