package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.*;
import cn.movie.robot.enums.ProjectMemberTypeEnum;
import cn.movie.robot.enums.ProjectStateEnum;
import cn.movie.robot.model.*;
import cn.movie.robot.service.IOplogService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.oplog.*;
import cn.movie.robot.vo.resp.PageBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ObjectRemoved;
import org.javers.core.metamodel.object.GlobalId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

  @Resource
  ContractSubjectRepository contractSubjectRepository;

  @Resource
  StaffRepository staffRepository;

  @Resource
  OperationLogRepository operationLogRepository;

  @Resource
  ProjectDetailRepository projectDetailRepository;

  @Resource
  FeeCategoryRepository feeCategoryRepository;

  @Resource
  ProviderRepository providerRepository;

  @Override
  public Result list(Pageable pageable, int targetId, int targetType) {
    Page<OperationLog> operationLogPage = operationLogRepository.queryAllByTargetIdAndTargetType(targetId, targetType, pageable);
    PageBean<OperationLog> operationLogPageBean = new PageBean<>(
        operationLogPage.getTotalElements(),
        operationLogPage.getTotalPages(),
        operationLogPage.getContent()
    );
    return Result.succ(operationLogPageBean);
  }

  @Override
  public void saveBaseInfoOplog(ProjectBaseInfoOplog newBaseInfoOplog, ProjectBaseInfoOplog oldBaseInfoOplog) {
    Javers javers = JaversBuilder.javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
        .build();

    Diff diff = javers.compare(oldBaseInfoOplog, newBaseInfoOplog);

    StringBuilder info = new StringBuilder();

    diff.groupByObject().forEach(byObject -> {
      String byObjectChange = byObject.toString();
      if (byObject.getGlobalId().getTypeName().equals("项目成员")){
        for (Change change : byObject.get()){
          if (change instanceof NewObject || change instanceof ObjectRemoved){
            ProjectMemberOplog op = (ProjectMemberOplog) change.getAffectedObject().get();
            byObjectChange = byObjectChange.replace("\n", " ");
            byObjectChange += op.getMemberList().toString();
          }
        }
      }
      info.append(byObjectChange);
      info.append("\n");
    });
    String result = dealEnToCn(info.toString());
    if (StringUtils.isNoneEmpty(result)){
      saveOplog(newBaseInfoOplog.getId(), Constants.OPERATION_LOG_TYPE_BASE_INFO, result);
    }
  }

  @Override
  public void saveShootingOplog(ProjectShootingDetailOplog newOplog, ProjectShootingDetailOplog oldOplog) {
    Javers javers = JaversBuilder.javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
        .build();

    Diff diff = javers.compare(oldOplog, newOplog);

    StringBuilder info = new StringBuilder();

    diff.groupByObject().forEach(byObject -> {
      String byObjectChange = byObject.toString();
      if (byObject.getGlobalId().getTypeName().equals("费用项")){
        for (Change change : byObject.get()){
          if (change instanceof NewObject || change instanceof ObjectRemoved){
            ProjectFeeDetailOplog op = (ProjectFeeDetailOplog) change.getAffectedObject().get();
            byObjectChange = byObjectChange.replace("\n", " ");
            byObjectChange += op.toString();
          }
        }
      }
      info.append(byObjectChange);
      info.append("\n");
    });
    String result = dealEnToCn(info.toString());
    if (StringUtils.isNoneEmpty(result)){
      saveOplog(newOplog.getProjectId(), Constants.OPERATION_LOG_TYPE_SHOOTING, result);
    }
  }

  @Override
  public void saveLastStateOplog(ProjectLastStateDetailOplog newOplog, ProjectLastStateDetailOplog oldOplog) {
    Javers javers = JaversBuilder.javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
        .build();

    Diff diff = javers.compare(oldOplog, newOplog);

    StringBuilder info = new StringBuilder();

    diff.groupByObject().forEach(byObject -> {
      String byObjectChange = byObject.toString();
      info.append(byObjectChange);
      info.append("\n");
    });
    String result = dealEnToCn(info.toString());
    if (StringUtils.isNoneEmpty(result)){
      saveOplog(newOplog.getProjectId(), Constants.OPERATION_LOG_TYPE_LAST_STATE, result);
    }
  }

  @Override
  public ProjectBaseInfoOplog buildBaseInfoOplog(int projectId) {
    Project project = projectRepository.getOne(projectId);

    ProjectBaseInfoOplog baseInfoOplog = new ProjectBaseInfoOplog(project);
    baseInfoOplog.setStateName(ProjectStateEnum.getStateName(project.getState()));
    if (Objects.nonNull(project.getContractSubjectId())){
      ContractSubject contractSubject = contractSubjectRepository.getOne(project.getContractSubjectId());
      baseInfoOplog.setContractSubjectName(contractSubject.getName());
    }

    List<ProjectMember> projectMemberList = projectMemberRepository.queryByProjectId(projectId);
    List<Staff> staffList = staffRepository.queryByIdIn(projectMemberList.stream().map(ProjectMember::getStaffId).collect(Collectors.toList()));
    HashMap<String, List<String>> memberHash = new HashMap<>();
    HashMap<Integer, String> staffNameHash = new HashMap<>(staffList.size());

    // 生成员工id-名称 hash
    staffList.forEach(staff -> staffNameHash.put(staff.getId(), staff.getName()));

    //生成member类型-员工名称 hash
    for (ProjectMember projectMember : projectMemberList){
      String memberTypeName = ProjectMemberTypeEnum.getTypeName(projectMember.getMemberType());
      List<String> memberNameList = memberHash.get(memberTypeName);
      if (Objects.isNull(memberNameList)){
        memberNameList = new ArrayList<String>();
      }
      memberNameList.add(staffNameHash.get(projectMember.getStaffId()));
      memberHash.put(memberTypeName, memberNameList);
    }

    List<ProjectMemberOplog> projectMemberOplogs = new ArrayList<>();
    for (String memberTypeName : memberHash.keySet()){
      ProjectMemberOplog projectMemberOplog = new ProjectMemberOplog();
      projectMemberOplog.setMemberType(memberTypeName);
      projectMemberOplog.setMemberList(memberHash.get(memberTypeName));
      projectMemberOplogs.add(projectMemberOplog);
    }

    baseInfoOplog.setProjectMemberOplogs(projectMemberOplogs);

    return baseInfoOplog;
  }

  @Override
  public ProjectShootingDetailOplog buildShootingOplog(int projectId) {
    Project project = projectRepository.getOne(projectId);
    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdAndStage(projectId, Constants.PROJECT_DETAIL_STATG_SHOOTING);

    ProjectShootingDetailOplog oplog = new ProjectShootingDetailOplog();
    oplog.setProjectId(projectId);
    oplog.setProjectFeeDetailOplogList(buildFeeDetailOplog(project, projectDetailList));
    return oplog;
  }

  @Override
  public ProjectLastStateDetailOplog buildLastStateOplog(int projectId) {
    Project project = projectRepository.getOne(projectId);
    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdAndStage(projectId, Constants.PROJECT_DETAIL_STATG_LAST_STATE);

    ProjectLastStateDetailOplog oplog = new ProjectLastStateDetailOplog();
    oplog.setProjectId(projectId);
    oplog.setProjectFeeDetailOplogList(buildFeeDetailOplog(project, projectDetailList));
    return oplog;
  }

  /**
   * 通过projectid构建费用明细Oplog
   * @param project
   * @param projectDetailList
   * @return
   */
  private List<ProjectFeeDetailOplog> buildFeeDetailOplog(Project project, List<ProjectDetail> projectDetailList){
    List<FeeCategory> parentFeeList = feeCategoryRepository.queryByIdIn(projectDetailList.stream().map(ProjectDetail::getFeeCategoryId).collect(Collectors.toList()));
    List<FeeCategory> childFeeList = feeCategoryRepository.queryByIdIn(projectDetailList.stream().map(ProjectDetail::getFeeChildCategoryId).collect(Collectors.toList()));
    List<Provider> providerList = providerRepository.queryByIdIn(projectDetailList.stream().map(ProjectDetail::getProviderId).collect(Collectors.toList()));

    // 费用项 id-名称 hash
    HashMap<Integer, String> parentFeeNameHash = new HashMap<>(parentFeeList.size());
    HashMap<Integer, String> childFeeNameHash = new HashMap<>(childFeeList.size());
    parentFeeList.forEach(feeCategory -> parentFeeNameHash.put(feeCategory.getId(), feeCategory.getName()));
    childFeeList.forEach(feeCategory -> childFeeNameHash.put(feeCategory.getId(), feeCategory.getName()));

    // 供应商 id-名称 hash
    HashMap<Integer, String> providerNameHash = new HashMap<>(projectDetailList.size());
    providerList.forEach(provider -> providerNameHash.put(provider.getId(), provider.getName()));

    List<ProjectFeeDetailOplog> projectFeeDetailOplogs = new ArrayList<>();

    for (ProjectDetail projectDetail : projectDetailList){
      ProjectFeeDetailOplog detailOplog = new ProjectFeeDetailOplog();
      detailOplog.setBudgetAmount(projectDetail.getBudgetAmount());
      detailOplog.setRealAmount(projectDetail.getRealAmount());
      detailOplog.setRankScore(projectDetail.getRankScore());
      detailOplog.setRemark(projectDetail.getRemark());
      detailOplog.setFeeCategoryName(parentFeeNameHash.get(projectDetail.getFeeCategoryId()));
      detailOplog.setFeeChildCategoryName(childFeeNameHash.get(projectDetail.getFeeChildCategoryId()));
      detailOplog.setProviderName(providerNameHash.get(projectDetail.getProviderId()));
      projectFeeDetailOplogs.add(detailOplog);
    }

    return projectFeeDetailOplogs;
  }

  private String dealEnToCn(String result){
    result = result.replaceAll("change to", "变更为");
    result = result.replaceAll("object removed", "删除");
    result = result.replaceAll("new object", "新增");
    result = result.replaceAll("collection changes", "变更");
    result = result.replaceAll("added", "新增");
    result = result.replaceAll("removed", "删除");
    result = result.replaceAll("changes on", "");
    result = result.replaceAll("changes to", "变为");
    result = result.replaceAll("changed to", "变为");
    result = result.replaceAll("value changed from", "从");
    result = result.replaceAll("to", "变为");
    return result;
  }

  private void saveOplog(int projectId, int targetType, String log){
    Subject subject = SecurityUtils.getSubject();
    User user = new User();
    try {
      BeanUtils.copyProperties(user, subject.getPrincipal());
    } catch (Exception e) {
    }
    OperationLog operationLog = new OperationLog();
    operationLog.setOperatorId(user.getId());
    operationLog.setOperatorName(user.getName());
    operationLog.setOperationInfo(log);
    operationLog.setTargetId(projectId);
    operationLog.setTargetType(targetType);
    operationLogRepository.save(operationLog);
  }
}
