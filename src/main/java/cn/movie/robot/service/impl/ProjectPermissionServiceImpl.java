package cn.movie.robot.service.impl;

import cn.movie.robot.dao.ProjectPermissionRepository;
import cn.movie.robot.dao.ProjectRepository;
import cn.movie.robot.dao.UserRepository;
import cn.movie.robot.model.Project;
import cn.movie.robot.model.ProjectPermission;
import cn.movie.robot.model.User;
import cn.movie.robot.service.IProjectPermissionService;
import cn.movie.robot.utils.SessionUtil;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ProjectPermissionVo;
import cn.movie.robot.vo.resp.ProjectPermissionRespVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2020/2/4
 */
@Slf4j
@Service
public class ProjectPermissionServiceImpl implements IProjectPermissionService {

  @Resource
  ProjectPermissionRepository projectPermissionRepository;

  @Resource
  UserRepository userRepository;

  @Resource
  ProjectRepository projectRepository;

  @Override
  public Result list(Integer projectId, Integer permissionType) {
    List<ProjectPermission> projectPermissionList = projectPermissionRepository.findByProjectIdAndAndPermissionType(projectId, permissionType);

    List<Integer> userIds = projectPermissionList.stream().map(ProjectPermission::getUserId).collect(Collectors.toList());
    List<User> users = userRepository.findByIdIn(userIds);
    HashMap<Integer, String> userNames = dealUserNames(users);

    List<ProjectPermissionRespVo> projectPermissionVoList = new ArrayList<>();
    for (ProjectPermission permission : projectPermissionList){
      ProjectPermissionRespVo vo = new ProjectPermissionRespVo();
      vo.setId(permission.getId());
      vo.setPermissionType(permission.getPermissionType());
      vo.setUserId(permission.getUserId());
      vo.setProjectId(permission.getProjectId());
      vo.setUserName(userNames.get(permission.getUserId()));
      projectPermissionVoList.add(vo);
    }

    return Result.succ(projectPermissionVoList);
  }

  @Transactional
  @Override
  public Result update(Integer projectId, ProjectPermissionVo projectPermissionVo) {
    Project project = projectRepository.getOne(projectId);
    if (ObjectUtils.isEmpty(project)){
      return Result.fail("无此项目");
    }

    List<ProjectPermission> existProjectPermissions = projectPermissionRepository.findByProjectIdAndAndPermissionType(
        projectId, projectPermissionVo.getPermissionType()
    );

    List<Integer> existUserIds = existProjectPermissions.stream().map(ProjectPermission::getUserId).collect(Collectors.toList());
    List<Integer> needAddUserIds = new ArrayList<>();
    List<Integer> needDeleteUserIds = new ArrayList<>();

    for (Integer newId : projectPermissionVo.getUserIds()){
      if (!existUserIds.contains(newId)){
        needAddUserIds.add(newId);
      }
    }

    for (Integer oldId : existUserIds){
      if (!projectPermissionVo.getUserIds().contains(oldId)){
        needDeleteUserIds.add(oldId);
      }
    }

    if (needDeleteUserIds.size() > 0){
      projectPermissionRepository.deleteByProjectIdAndPermissionTypeAndUserIdIn(
          projectId, projectPermissionVo.getPermissionType(), needDeleteUserIds
      );
    }

    if (needAddUserIds.size() > 0){
      Integer operatorId = SessionUtil.getCurrentUserId();
      for (Integer userId : needAddUserIds){
        ProjectPermission projectPermission = new ProjectPermission();
        projectPermission.setPermissionType(projectPermissionVo.getPermissionType());
        projectPermission.setProjectId(projectId);
        projectPermission.setUserId(userId);
        projectPermission.setOperatorId(operatorId);
        projectPermissionRepository.save(projectPermission);
      }
    }

    return Result.succ();
  }

  @Override
  public List<Integer> queryUserAvailProjectIds(Integer permissionType){
    Integer userId = SessionUtil.getCurrentUserId();
    List<ProjectPermission> projectPermissions = projectPermissionRepository.findByUserIdAndPermissionType(userId, permissionType);
    return projectPermissions.stream().map(ProjectPermission::getProjectId).collect(Collectors.toList());
  }

  private HashMap<Integer, String> dealUserNames(List<User> users){
    HashMap<Integer, String> userNames = new HashMap<>();
    for (User user : users){
      userNames.put(user.getId(), user.getName());
    }
    return userNames;
  }
}
