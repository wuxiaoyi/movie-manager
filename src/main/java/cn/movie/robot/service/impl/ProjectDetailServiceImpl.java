package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.FeeCategoryRepository;
import cn.movie.robot.dao.ProjectDetailRepository;
import cn.movie.robot.dao.ProjectRepository;
import cn.movie.robot.enums.ProjectStateEnum;
import cn.movie.robot.model.FeeCategory;
import cn.movie.robot.model.Project;
import cn.movie.robot.model.ProjectDetail;
import cn.movie.robot.service.IProjectAmountService;
import cn.movie.robot.service.IProjectDetailService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.project.ProjectFeeDetailVo;
import cn.movie.robot.vo.resp.ProjectDetailRespVo;
import cn.movie.robot.vo.resp.ProjectFeeRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@Service
public class ProjectDetailServiceImpl implements IProjectDetailService {

  @Resource
  ProjectRepository projectRepository;

  @Resource
  FeeCategoryRepository feeCategoryRepository;

  @Resource
  ProjectDetailRepository projectDetailRepository;

  @Autowired
  IProjectAmountService projectAmountService;

  @Override
  public Result shootingDetail(Integer projectId) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return Result.error("该项目不存在");
    }

    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdAndStageAndFeeChildCategoryIdIsNotNull(projectId, Constants.PROJECT_DETAIL_STATG_SHOOTING);

    return Result.succ(buildProjectDetailResp(project, projectDetailList));
  }

  @Override
  public Result lastStateDetail(Integer projectId) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return Result.error("该项目不存在");
    }

    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdAndStageAndFeeChildCategoryIdIsNotNull(projectId, Constants.PROJECT_DETAIL_STATG_LAST_STATE);

    return Result.succ(buildProjectDetailResp(project, projectDetailList));
  }

  @Override
  public Result saveShottingInfo(int projectId, List<ProjectFeeDetailVo> projectFeeDetailVoList) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return Result.error("该项目不存在");
    }
    if (ProjectStateEnum.isCancel(project.getState()) || ProjectStateEnum.isPause(project.getState())){
      return Result.error("该项目不可编辑");
    }

    int projectStage = Constants.PROJECT_DETAIL_STATG_SHOOTING;
    updateDetails(projectId, projectFeeDetailVoList, projectStage);
    projectAmountService.refreshAmount(projectId);
    refreshAmount(projectId);
    return Result.succ();
  }

  @Override
  public Result saveLastStateInfo(int projectId, List<ProjectFeeDetailVo> projectFeeDetailVoList) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return Result.error("该项目不存在");
    }
    if (ProjectStateEnum.isCancel(project.getState()) || ProjectStateEnum.isPause(project.getState())){
      return Result.error("该项目不可编辑");
    }

    int projectStage = Constants.PROJECT_DETAIL_STATG_LAST_STATE;
    updateDetails(projectId, projectFeeDetailVoList, projectStage);
    projectAmountService.refreshAmount(projectId);
    refreshAmount(projectId);

    return Result.succ();
  }

  @Override
  public List<ProjectDetail> initShootingInfo(Integer projectId) {
    return initByProjectIdAndStage(projectId, Constants.PROJECT_DETAIL_STATG_SHOOTING);
  }

  @Override
  public List<ProjectDetail> initLastStateInfo(Integer projectId) {
    return initByProjectIdAndStage(projectId, Constants.PROJECT_DETAIL_STATG_LAST_STATE);
  }

  private ProjectDetailRespVo buildProjectDetailResp(Project project, List<ProjectDetail> projectDetailList){
    ProjectDetailRespVo projectDetailRespVo = new ProjectDetailRespVo();
    projectDetailRespVo.setProjectId(project.getId());
    projectDetailRespVo.setProjectName(project.getName());
    projectDetailRespVo.setProjectSid(project.getSid());

    List<ProjectFeeRespVo> projectFeeRespVos = new ArrayList<>();
    for (ProjectDetail projectDetail : projectDetailList){
      ProjectFeeRespVo projectFeeRespVo = new ProjectFeeRespVo();
      projectFeeRespVo.setId(projectDetail.getId());
      projectFeeRespVo.setBudgetAmount(projectDetail.getBudgetAmount());
      projectFeeRespVo.setRealAmount(projectDetail.getRealAmount());
      projectFeeRespVo.setFeeCategoryId(projectDetail.getFeeCategoryId());
      projectFeeRespVo.setFeeChildCategoryId(projectDetail.getFeeChildCategoryId());
      projectFeeRespVo.setProviderId(projectDetail.getProviderId());
      projectFeeRespVo.setRankScore(projectDetail.getRankScore());
      projectFeeRespVo.setRemark(projectDetail.getRemark());
      projectFeeRespVo.setStage(projectDetail.getStage());
      projectFeeRespVos.add(projectFeeRespVo);
    }
    projectDetailRespVo.setProjectFees(projectFeeRespVos);
    return projectDetailRespVo;
  }

  private List<ProjectDetail> initByProjectIdAndStage(Integer projectId, Integer stage){
    List<ProjectDetail> projectDetailList = new ArrayList<>();

    List<FeeCategory> parentFeeCategoryList = feeCategoryRepository.queryByStageAndStateAndCategoryType(
        stage,
        Constants.COMMON_STATE_NORMAL,
        Constants.FEE_CATEGORY_TYPE_PARENT
    );
    for (FeeCategory feeCategory : parentFeeCategoryList){
      ProjectDetail projectDetail = new ProjectDetail();
      projectDetail.setProjectId(projectId);
      projectDetail.setFeeCategoryId(feeCategory.getId());
      projectDetail.setStage(feeCategory.getStage());
      projectDetailRepository.save(projectDetail);
      projectDetailList.add(projectDetail);
    }

    List<Integer> validParentCategoryIds = parentFeeCategoryList.stream().map(FeeCategory::getId).collect(Collectors.toList());

    List<FeeCategory> feeCategoryList = feeCategoryRepository.queryByStageAndStateAndCategoryType(
        stage,
        Constants.COMMON_STATE_NORMAL,
        Constants.FEE_CATEGORY_TYPE_CHILD
    );

    for (FeeCategory feeCategory : feeCategoryList){
      if (validParentCategoryIds.contains(feeCategory.getParentCategoryId())){
        ProjectDetail projectDetail = new ProjectDetail();
        projectDetail.setProjectId(projectId);
        projectDetail.setFeeCategoryId(feeCategory.getParentCategoryId());
        projectDetail.setFeeChildCategoryId(feeCategory.getId());
        projectDetail.setStage(feeCategory.getStage());
        projectDetailRepository.save(projectDetail);
        projectDetailList.add(projectDetail);
      }
    }

    return projectDetailList;
  }

  private void refreshAmount(int projectId){
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

  private void updateDetails(int projectId, List<ProjectFeeDetailVo> projectFeeDetailVoList, int projectStage){
    List<ProjectDetail> existDetails = projectDetailRepository.queryByProjectIdAndStage(projectId, projectStage);

    List<ProjectDetail> existChildFeeDetails = existDetails.stream()
        .filter(projectDetail -> Objects.nonNull(projectDetail.getFeeChildCategoryId()))
        .collect(Collectors.toList());
    List<Integer> existChildDetailIds = existChildFeeDetails.stream()
        .map(ProjectDetail::getId).collect(Collectors.toList());

    List<ProjectDetail> existParentFeeDetails = existDetails.stream()
        .filter(projectDetail -> Objects.isNull(projectDetail.getFeeChildCategoryId()))
        .collect(Collectors.toList());
    List<Integer> existParentFeeCategoryIds = existParentFeeDetails.stream()
        .map(ProjectDetail::getFeeCategoryId).distinct().collect(Collectors.toList());

    List<Integer> updateDetailIds = projectFeeDetailVoList.stream()
        .filter(projectFeeDetailVo -> Objects.nonNull(projectFeeDetailVo.getId()))
        .map(ProjectFeeDetailVo::getId).collect(Collectors.toList());

    // 删除
    existChildDetailIds.removeAll(updateDetailIds);
    if (existChildDetailIds.size() > 0){
      projectDetailRepository.deleteByIdIn(existChildDetailIds);
    }

    // 更新 or 新增
    for (ProjectFeeDetailVo projectFeeDetailVo : projectFeeDetailVoList){
      ProjectDetail projectDetail;
      if (Objects.nonNull(projectFeeDetailVo.getId())){
        projectDetail = projectDetailRepository.getOne(projectFeeDetailVo.getId());
      }else {
        projectDetail = new ProjectDetail();
        projectDetail.setStage(projectStage);
        projectDetail.setProjectId(projectId);
      }
      if (Objects.isNull(projectDetail)){
        continue;
      }
      projectDetail.setFeeCategoryId(projectFeeDetailVo.getFeeCategoryId());
      projectDetail.setFeeChildCategoryId(projectFeeDetailVo.getFeeChildCategoryId());
      projectDetail.setBudgetAmount(projectFeeDetailVo.getBudgetAmount());
      projectDetail.setRealAmount(projectFeeDetailVo.getRealAmount());
      projectDetail.setProviderId(projectFeeDetailVo.getProviderId());
      projectDetail.setRankScore(projectFeeDetailVo.getRankScore());
      projectDetail.setRemark(projectFeeDetailVo.getRemark());
      projectDetailRepository.save(projectDetail);
    }

    // 新增parent fee detail，因为编辑时可能新增了该项目不存在的新二级费用项
    List<Integer> requestParentFeeCategoryIds = projectFeeDetailVoList.stream().map(ProjectFeeDetailVo::getFeeCategoryId).distinct().collect(Collectors.toList());
    requestParentFeeCategoryIds.removeAll(existParentFeeCategoryIds);
    if (requestParentFeeCategoryIds.size() > 0){
      for (Integer feeCategoryId : requestParentFeeCategoryIds){
        ProjectDetail projectDetail = new ProjectDetail();
        projectDetail.setStage(projectStage);
        projectDetail.setProjectId(projectId);
        projectDetail.setFeeCategoryId(feeCategoryId);
        projectDetail.setBudgetAmount(BigDecimal.ZERO);
        projectDetail.setRealAmount(BigDecimal.ZERO);
        projectDetailRepository.save(projectDetail);
      }
    }

  }
}
