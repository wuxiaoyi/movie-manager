package cn.movie.robot.service.impl;

import cn.movie.robot.dao.ProjectMemberRepository;
import cn.movie.robot.model.ProjectMember;
import cn.movie.robot.service.IProjectMemberService;
import cn.movie.robot.vo.req.project.ProjectMemberVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
@Service
public class ProjectMemberServiceImpl implements IProjectMemberService {

  @Resource
  ProjectMemberRepository projectMemberRepository;

  @Override
  public boolean saveProjectMembers(int projectId, List<ProjectMemberVo> projectMemberVoList) {
    List<Integer> newMemberIds = projectMemberVoList.stream()
        .filter(projectMemberVo -> Objects.nonNull(projectMemberVo.getId()))
        .map(ProjectMemberVo::getId)
        .collect(Collectors.toList());

    List<ProjectMember> existProjectMembers = projectMemberRepository.queryByProjectId(projectId);
    List<Integer> existMemberIds = existProjectMembers.stream().map(ProjectMember::getId).collect(Collectors.toList());
    existMemberIds.removeAll(newMemberIds);

    /**
     * 删除
     */
    if (existMemberIds.size() > 0){
      projectMemberRepository.deleteByIdIn(existMemberIds);
    }

    /**
     * 新增或修改
     */
    for (ProjectMemberVo projectMemberVo : projectMemberVoList){
      if (Objects.isNull(projectMemberVo.getId())){
        if (!isExistRecord(projectMemberVo)){
          save(projectMemberVo);
        }
      }else {
        ProjectMember projectMember = projectMemberRepository.getOne(projectMemberVo.getId());
        if (Objects.nonNull(projectMember)){
          projectMember.setStaffId(projectMemberVo.getStaffId());
          projectMember.setMemberType(projectMemberVo.getMemberType());
          projectMemberRepository.save(projectMember);
        }
      }
    }

    return true;
  }

  private ProjectMember save(ProjectMemberVo projectMemberVo){
    ProjectMember projectMember = new ProjectMember();
    projectMember.setProjectId(projectMemberVo.getProjectId());
    projectMember.setStaffId(projectMemberVo.getStaffId());
    projectMember.setMemberType(projectMemberVo.getMemberType());
    projectMemberRepository.save(projectMember);
    return projectMember;
  }

  private boolean isExistRecord(ProjectMemberVo projectMemberVo){
    ProjectMember projectMember = projectMemberRepository.findByProjectIdAndStaffIdAndMemberType(
        projectMemberVo.getProjectId(),
        projectMemberVo.getStaffId(),
        projectMemberVo.getMemberType()
    );
    return Objects.nonNull(projectMember);
  }
}
