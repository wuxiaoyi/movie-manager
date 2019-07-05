package cn.movie.robot.service.impl;

import cn.movie.robot.dao.ProjectRepository;
import cn.movie.robot.model.Project;
import cn.movie.robot.service.IOplogService;
import cn.movie.robot.service.IProjectDetailService;
import cn.movie.robot.service.IProjectMemberService;
import cn.movie.robot.service.IProjectService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.project.ProjectBaseInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Objects;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@Service
public class ProjectServiceImpl implements IProjectService {

  @Resource
  ProjectRepository projectRepository;

  @Autowired
  IProjectMemberService projectMemberService;

  @Autowired
  IProjectDetailService projectDetailService;

  @Autowired
  IOplogService oplogService;

  @Override
  @Transactional(rollbackOn = Exception.class)
  public Result create(String name) {
    Project existProject = projectRepository.findByName(name);
    if (Objects.nonNull(existProject)){
      return Result.error("该项目名称已存在");
    }
    Project project = new Project();
    project.setName(name);
    projectRepository.save(project);
    projectDetailService.initShootingInfo(project.getId());
    projectDetailService.initLastStateInfo(project.getId());

    return Result.succ(project.getId());
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public Result saveBaseInfo(int projectId, ProjectBaseInfoVo projectBaseInfoVo) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return Result.error("该项目不存在");
    }

    project.setSid(projectBaseInfoVo.getSid());
    project.setName(projectBaseInfoVo.getName());
    project.setFilmDuration(projectBaseInfoVo.getFilmDuration());
    project.setContractSubjectId(projectBaseInfoVo.getContractSubjectId());
    project.setShootingStartAt(projectBaseInfoVo.getShootingStartAt());
    project.setShootingDuration(projectBaseInfoVo.getShootingDuration());
    projectRepository.save(project);

    projectMemberService.saveProjectMembers(projectId, projectBaseInfoVo.getProjectMembers());

    return Result.succ();
  }
}
