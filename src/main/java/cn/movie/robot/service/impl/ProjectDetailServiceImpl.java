package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.FeeCategoryRepository;
import cn.movie.robot.dao.ProjectDetailRepository;
import cn.movie.robot.dao.ProjectRepository;
import cn.movie.robot.model.FeeCategory;
import cn.movie.robot.model.Project;
import cn.movie.robot.model.ProjectDetail;
import cn.movie.robot.service.IProjectDetailService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.project.ProjectFeeDetailVo;
import cn.movie.robot.vo.resp.ProjectDetailRespVo;
import cn.movie.robot.vo.resp.ProjectFeeRespVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
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

  @Override
  public Result shootingDetail(Integer projectId) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return Result.error("该项目不存在");
    }

    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdAndStage(projectId, Constants.PROJECT_DETAIL_STATG_SHOOTING);

    return Result.succ(buildProjectDetailResp(project, projectDetailList));
  }

  @Override
  public Result lastStateDetail(Integer projectId) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return Result.error("该项目不存在");
    }

    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdAndStage(projectId, Constants.PROJECT_DETAIL_STATG_LAST_STATE);

    return Result.succ(buildProjectDetailResp(project, projectDetailList));
  }

  @Override
  @Transactional
  public Result saveShottingInfo(int projectId, List<ProjectFeeDetailVo> projectFeeDetailVoList) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return Result.error("该项目不存在");
    }

    int projectStage = Constants.PROJECT_DETAIL_STATG_SHOOTING;
    updateDetails(projectId, projectFeeDetailVoList, projectStage);

    return Result.succ();
  }

  @Override
  public Result saveLastStateInfo(int projectId, List<ProjectFeeDetailVo> projectFeeDetailVoList) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return Result.error("该项目不存在");
    }

    int projectStage = Constants.PROJECT_DETAIL_STATG_LAST_STATE;
    updateDetails(projectId, projectFeeDetailVoList, projectStage);

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
    List<FeeCategory> feeCategoryList = feeCategoryRepository.queryByStageAndStateAndCategoryType(
        stage,
        Constants.COMMON_STATE_NORMAL,
        Constants.FEE_CATEGORY_TYPE_CHILD
    );

    for (FeeCategory feeCategory : feeCategoryList){
      ProjectDetail projectDetail = new ProjectDetail();
      projectDetail.setProjectId(projectId);
      projectDetail.setFeeCategoryId(feeCategory.getParentCategoryId());
      projectDetail.setFeeChildCategoryId(feeCategory.getId());
      projectDetail.setStage(feeCategory.getStage());
      projectDetailRepository.save(projectDetail);
      projectDetailList.add(projectDetail);
    }
    return projectDetailList;
  }

  private void updateDetails(int projectId, List<ProjectFeeDetailVo> projectFeeDetailVoList, int projectStage){
    List<ProjectDetail> existDetails = projectDetailRepository.queryByProjectIdAndStage(projectId, projectStage);
    List<Integer> existDetailIds = existDetails.stream()
        .map(ProjectDetail::getId).collect(Collectors.toList());
    List<Integer> updateDetailIds = projectFeeDetailVoList.stream()
        .filter(projectFeeDetailVo -> Objects.nonNull(projectFeeDetailVo.getId()))
        .map(ProjectFeeDetailVo::getId).collect(Collectors.toList());

    existDetailIds.removeAll(updateDetailIds);
    // 删除
    if (existDetailIds.size() > 0){
      projectDetailRepository.deleteByIdIn(existDetailIds);
    }

    for (ProjectFeeDetailVo projectFeeDetailVo : projectFeeDetailVoList){
      ProjectDetail projectDetail;
      // 更新 or 新增
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
  }
}
