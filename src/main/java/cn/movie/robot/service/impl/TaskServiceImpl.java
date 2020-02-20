package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.*;
import cn.movie.robot.model.*;
import cn.movie.robot.service.ITaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/9/11
 */
@Service
public class TaskServiceImpl implements ITaskService {

  @Resource
  ProjectDetailRepository projectDetailRepository;

  @Resource
  ProjectRepository projectRepository;

  @Resource
  OperationLogRepository operationLogRepository;

  @Resource
  ProjectPermissionRepository projectPermissionRepository;

  @Resource
  UserRepository userRepository;

  @Override
  public void refreshProjectAmount(Integer projectId) {
    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectId(projectId);
    List<ProjectDetail> parentCategoryDetailList = projectDetailList.stream()
        .filter(projectDetail -> Objects.isNull(projectDetail.getFeeChildCategoryId()))
        .collect(Collectors.toList());
    List<ProjectDetail> childCategoryDetailList = projectDetailList.stream()
        .filter(projectDetail -> Objects.nonNull(projectDetail.getFeeChildCategoryId()))
        .collect(Collectors.toList());

    for (ProjectDetail parentCategoryDetail : parentCategoryDetailList){
      BigDecimal budgetAmount = new BigDecimal(0);
      BigDecimal realAmount = new BigDecimal(0);
      for (ProjectDetail childDetail : childCategoryDetailList){
        if (childDetail.getFeeCategoryId().equals(parentCategoryDetail.getFeeCategoryId())){
          budgetAmount = budgetAmount.add(childDetail.getBudgetAmount());
          realAmount = realAmount.add(childDetail.getRealAmount());
        }
      }
      parentCategoryDetail.setRealAmount(realAmount);
      parentCategoryDetail.setBudgetAmount(budgetAmount);
      projectDetailRepository.save(parentCategoryDetail);
    }
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void fixProjectCreator() {
    List<Project> projectList = projectRepository.findAll();
    for (Project project : projectList){
      if (project.getCreatorId() == null || project.getCreatorId() == 0){
        OperationLog log = operationLogRepository.findFirstByTargetIdAndTargetType(
            project.getId(), Constants.OPERATION_LOG_TYPE_BASE_INFO
        );
        if (!ObjectUtils.isEmpty(log)){

          User user = userRepository.getOne(log.getOperatorId());

          int creatorId = 1;
          if (!ObjectUtils.isEmpty(user)){
            creatorId = user.getId();
          }

          project.setCreatorId(creatorId);
          projectRepository.save(project);

          ProjectPermission readPermission = projectPermissionRepository.findFirstByUserIdAndProjectIdAndPermissionType(
              project.getCreatorId(), project.getId(), Constants.PROJECT_PERMISSION_READ
          );
          if (ObjectUtils.isEmpty(readPermission)){
            ProjectPermission newReadPermission = new ProjectPermission();
            newReadPermission.setUserId(project.getCreatorId());
            newReadPermission.setProjectId(project.getId());
            newReadPermission.setOperatorId(1);
            newReadPermission.setPermissionType(Constants.PROJECT_PERMISSION_READ);
            projectPermissionRepository.save(newReadPermission);
          }

          ProjectPermission writePermission = projectPermissionRepository.findFirstByUserIdAndProjectIdAndPermissionType(
              project.getCreatorId(), project.getId(), Constants.PROJECT_PERMISSION_WRITE
          );
          if (ObjectUtils.isEmpty(writePermission)){
            ProjectPermission newWritePermission = new ProjectPermission();
            newWritePermission.setUserId(project.getCreatorId());
            newWritePermission.setProjectId(project.getId());
            newWritePermission.setOperatorId(1);
            newWritePermission.setPermissionType(Constants.PROJECT_PERMISSION_WRITE);
            projectPermissionRepository.save(newWritePermission);
          }
        }
      }
    }
  }
}
