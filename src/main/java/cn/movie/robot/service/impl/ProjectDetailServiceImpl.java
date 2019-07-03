package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.FeeCategoryRepository;
import cn.movie.robot.dao.ProjectDetailRepository;
import cn.movie.robot.model.FeeCategory;
import cn.movie.robot.model.ProjectDetail;
import cn.movie.robot.service.IProjectDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@Service
public class ProjectDetailServiceImpl implements IProjectDetailService {

  @Resource
  FeeCategoryRepository feeCategoryRepository;

  @Resource
  ProjectDetailRepository projectDetailRepository;

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
