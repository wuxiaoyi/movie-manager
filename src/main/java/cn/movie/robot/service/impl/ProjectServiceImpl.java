package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.ProjectMemberRepository;
import cn.movie.robot.dao.ProjectPermissionRepository;
import cn.movie.robot.dao.ProjectRepository;
import cn.movie.robot.enums.ProjectStateEnum;
import cn.movie.robot.model.Project;
import cn.movie.robot.model.ProjectMember;
import cn.movie.robot.model.ProjectPermission;
import cn.movie.robot.service.*;
import cn.movie.robot.utils.SessionUtil;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.project.ProjectBaseInfoVo;
import cn.movie.robot.vo.req.search.BaseSearchVo;
import cn.movie.robot.vo.req.search.ProjectSearchVo;
import cn.movie.robot.vo.resp.PageBean;
import cn.movie.robot.vo.resp.ProjectMemberRespVo;
import cn.movie.robot.vo.resp.ProjectRespVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

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
  IProjectPermissionService projectPermissionService;

  @Resource
  ProjectPermissionRepository projectPermissionRepository;

  @Autowired
  IOplogService oplogService;

  @Resource
  ProjectMemberRepository projectMemberRepository;

  @Override
  public Result queryAll(BaseSearchVo baseSearchVo) {
    Pageable pageable = PageRequest.of(baseSearchVo.getPage() - 1, baseSearchVo.getPageSize(), Sort.by(DESC, Constants.COMMON_FIELD_NAME_ID));
    Specification<Project> specification = buildBaseQuery(baseSearchVo);

    Page<Project> projectPage = projectRepository.findAll(specification, pageable);

    PageBean<ProjectRespVo> projectPageBean = new PageBean<>(
        projectPage.getTotalElements(),
        projectPage.getTotalPages(),
        dealProjectListInfo(projectPage.getContent())
    );
    return Result.succ(projectPageBean);
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public Result create(String name) {
    Project existProject = projectRepository.findByName(name);
    if (Objects.nonNull(existProject)) {
      return Result.error("该项目名称已存在");
    }
    Project project = new Project();
    project.setName(name);
    project.setCreatorId(SessionUtil.getCurrentUser().getId());
    projectRepository.save(project);
    projectDetailService.initShootingInfo(project.getId());
    projectDetailService.initLastStateInfo(project.getId());
    initProjectPermission(project);

    return Result.succ(project.getId());
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public Result saveBaseInfo(int projectId, ProjectBaseInfoVo projectBaseInfoVo) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)) {
      return Result.error("该项目不存在");
    }
    if (ProjectStateEnum.isCancel(project.getState()) || ProjectStateEnum.isPause(project.getState())) {
      return Result.error("该项目不可编辑");
    }

    project.setSid(projectBaseInfoVo.getSid());
    project.setName(projectBaseInfoVo.getName());
    project.setFilmDuration(projectBaseInfoVo.getFilmDuration());
    project.setContractSubjectId(projectBaseInfoVo.getContractSubjectId());
    project.setCompanyId(projectBaseInfoVo.getCompanyId());
    project.setChildCompanyId(projectBaseInfoVo.getChildCompanyId());
    project.setShootingStartAt(projectBaseInfoVo.getShootingStartAt());
    project.setShootingDuration(projectBaseInfoVo.getShootingDuration());
    project.setContractAmount(projectBaseInfoVo.getContractAmount());
    project.setReturnAmount(projectBaseInfoVo.getReturnAmount());
    projectRepository.save(project);

    projectMemberService.saveProjectMembers(projectId, projectBaseInfoVo.getProjectMembers());

    return Result.succ();
  }

  @Override
  public Result detail(int projectId) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)) {
      return Result.error("项目不存在");
    }
    List<ProjectMember> projectMemberList = projectMemberRepository.queryByProjectId(projectId);

    ProjectRespVo projectRespVo = new ProjectRespVo();
    projectRespVo.setId(project.getId());
    projectRespVo.setName(project.getName());
    projectRespVo.setSid(project.getSid());
    projectRespVo.setBudgetCost(project.getBudgetCost());
    projectRespVo.setContractAmount(project.getContractAmount());
    projectRespVo.setRealCost(project.getRealCost());
    projectRespVo.setReturnAmount(project.getReturnAmount());
    projectRespVo.setContractSubjectId(project.getContractSubjectId());
    projectRespVo.setCompanyId(project.getCompanyId());
    projectRespVo.setChildCompanyId(project.getChildCompanyId());
    projectRespVo.setFilmDuration(project.getFilmDuration());
    projectRespVo.setShootingBudget(project.getShootingBudget());
    projectRespVo.setShootingCost(project.getShootingCost());
    projectRespVo.setShootingDuration(project.getShootingDuration());
    projectRespVo.setShootingStartAt(project.getShootingStartAt());
    projectRespVo.setLateStateBudget(project.getLateStateBudget());
    projectRespVo.setLateStateCost(project.getLateStateCost());
    projectRespVo.setState(project.getState());
    projectRespVo.setCreatedAt(project.getCreatedAt());
    projectRespVo.setUpdatedAt(project.getUpdatedAt());

    List<ProjectMemberRespVo> projectMemberRespVos = new ArrayList<>();
    for (ProjectMember projectMember : projectMemberList) {
      ProjectMemberRespVo projectMemberRespVo = new ProjectMemberRespVo();
      projectMemberRespVo.setId(projectMember.getId());
      projectMemberRespVo.setProjectId(project.getId());
      projectMemberRespVo.setMemberType(projectMember.getMemberType());
      projectMemberRespVo.setStaffId(projectMember.getStaffId());
      projectMemberRespVos.add(projectMemberRespVo);
    }
    projectRespVo.setProjectMembers(projectMemberRespVos);
    return Result.succ(projectRespVo);
  }

  @Override
  public Result updateState(int projectId, Integer state) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)) {
      return Result.error("项目不存在");
    }
    if (!ProjectStateEnum.getStates().contains(state)) {
      return Result.error("状态不存在");
    }
    project.setState(state);
    projectRepository.save(project);
    return Result.succ();
  }

  private void initProjectPermission(Project project){
    ProjectPermission readPermission = new ProjectPermission();
    readPermission.buildByProject(project, Constants.PROJECT_PERMISSION_READ);
    ProjectPermission writePermission = new ProjectPermission();
    writePermission.buildByProject(project, Constants.PROJECT_PERMISSION_WRITE);
    projectPermissionRepository.save(readPermission);
    projectPermissionRepository.save(writePermission);
  }

  private List<ProjectRespVo> dealProjectListInfo(List<Project> projectList){
    List<ProjectRespVo> projectRespVoList = new ArrayList<>();
    Integer currentUserId = SessionUtil.getCurrentUserId();
    boolean canManageAllProject = SessionUtil.hasPermission(Constants.PROJECT_MANAGE_ALL_PERMISSION);
    List<ProjectPermission> projectPermissionList = projectPermissionRepository.findByUserIdAndPermissionType(
        currentUserId, Constants.PROJECT_PERMISSION_WRITE
    );
    List<Integer> availProjectIds = projectPermissionList.stream().map(ProjectPermission::getProjectId).collect(Collectors.toList());

    for (Project project : projectList){
      ProjectRespVo projectRespVo = new ProjectRespVo();
      projectRespVo.setId(project.getId());
      projectRespVo.setName(project.getName());
      projectRespVo.setSid(project.getSid());
      projectRespVo.setContractSubjectId(project.getContractSubjectId());
      projectRespVo.setCompanyId(project.getCompanyId());
      projectRespVo.setCreatorId(project.getCreatorId());
      projectRespVo.setChildCompanyId(project.getChildCompanyId());
      projectRespVo.setState(project.getState());
      projectRespVo.setCreatedAt(project.getCreatedAt());
      projectRespVo.setUpdatedAt(project.getUpdatedAt());
      if (canManageAllProject || currentUserId.equals(project.getCreatorId())){
        projectRespVo.setCanGrantPermission(true);
      }else {
        projectRespVo.setCanGrantPermission(false);
      }

      if (canManageAllProject || availProjectIds.contains(project.getId())){
        projectRespVo.setCanEdit(true);
      }else {
        projectRespVo.setCanEdit(false);
      }
      projectRespVoList.add(projectRespVo);
    }

    return projectRespVoList;
  }

  private Specification<Project> buildBaseQuery(BaseSearchVo baseSearchVo) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (StringUtils.isNoneEmpty(baseSearchVo.getSid())){
        predicates.add(criteriaBuilder.like(root.get("sid"), "%" + baseSearchVo.getSid() + "%"));
      }
      if (StringUtils.isNoneEmpty(baseSearchVo.getProjectName())){
        predicates.add(criteriaBuilder.like(root.get("name"), "%" + baseSearchVo.getProjectName() + "%"));
      }
      if (Objects.nonNull(baseSearchVo.getContractId())){
        predicates.add(criteriaBuilder.equal(root.get("contractSubjectId"), baseSearchVo.getContractId()));
      }
      if (Objects.nonNull(baseSearchVo.getState())){
        predicates.add(criteriaBuilder.equal(root.get("state"), baseSearchVo.getState()));
      }
      /**
       * 用户可见项目过滤
       */
      if (!SessionUtil.hasPermission(Constants.PROJECT_MANAGE_ALL_PERMISSION)){
        List<Integer> projectIds = projectPermissionService.queryUserAvailProjectIds(
            Constants.PROJECT_PERMISSION_READ
        );
        if (CollectionUtils.isEmpty(projectIds)){
          predicates.add(criteriaBuilder.equal(root.get("id"), 0));
        }else {
          predicates.add(root.<Integer>get("id").in(projectIds));
        }
      }
      return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
    };
  }

}