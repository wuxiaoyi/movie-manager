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
import cn.movie.robot.vo.req.project.ProjectLastStateInfoVo;
import cn.movie.robot.vo.req.project.ProjectShottingInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
  public Result saveShottingInfo(int projectId, ProjectShottingInfoVo projectShottingInfoVo) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return Result.error("该项目不存在");
    }
    return null;
  }

  @Override
  public Result saveLastStateInfo(int projectId, ProjectLastStateInfoVo projectLastStateInfoVo) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return Result.error("该项目不存在");
    }
    return null;
  }

  @Override
  public List<ProjectDetail> initShootingInfo(Integer projectId) {
    return initByProjectIdAndStage(projectId, Constants.PROJECT_DETAIL_STATG_SHOOTING);
  }

  @Override
  public List<ProjectDetail> initLastStateInfo(Integer projectId) {
    return initByProjectIdAndStage(projectId, Constants.PROJECT_DETAIL_STATG_LAST_STATE);
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
}
