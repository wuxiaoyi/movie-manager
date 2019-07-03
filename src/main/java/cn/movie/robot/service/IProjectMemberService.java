package cn.movie.robot.service;

import cn.movie.robot.vo.req.project.ProjectMemberVo;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
public interface IProjectMemberService {
  boolean saveProjectMembers(int projectId, List<ProjectMemberVo> projectMemberVoList);
}
