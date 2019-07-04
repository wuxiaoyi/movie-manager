package cn.movie.robot.service.impl;

import cn.movie.robot.dao.ProjectMemberRepository;
import cn.movie.robot.dao.ProjectRepository;
import cn.movie.robot.enums.ProjectStateEnum;
import cn.movie.robot.model.Project;
import cn.movie.robot.model.ProjectMember;
import cn.movie.robot.service.IOplogService;
import cn.movie.robot.vo.oplog.ProjectBaseInfoOplog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
@Service
public class OplogServiceImpl implements IOplogService {

  @Resource
  ProjectRepository projectRepository;

  @Resource
  ProjectMemberRepository projectMemberRepository;

  @Override
  public void saveBaseInfoOplog() {

  }

  @Override
  public ProjectBaseInfoOplog buildBaseInfoOplog(int projectId) {
    Project project = projectRepository.getOne(projectId);
    List<ProjectMember> projectMemberList = projectMemberRepository.queryByProjectId(projectId);

    ProjectBaseInfoOplog baseInfoOplog = new ProjectBaseInfoOplog(project);
    baseInfoOplog.setStateName(ProjectStateEnum.getStateName(project.getState()));

    return baseInfoOplog;
  }
}
