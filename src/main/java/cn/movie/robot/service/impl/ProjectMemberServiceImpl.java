package cn.movie.robot.service.impl;

import cn.movie.robot.service.IProjectMemberService;
import cn.movie.robot.vo.req.project.ProjectMemberVo;

import java.util.List;
import java.util.Objects;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
public class ProjectMemberServiceImpl implements IProjectMemberService {
  @Override
  public boolean saveProjectMembers(List<ProjectMemberVo> projectMemberVoList) {
    for (ProjectMemberVo projectMemberVo : projectMemberVoList){
      if (Objects.isNull(projectMemberVo.getId())){

      }else {

      }
    }
    return false;
  }
}
