package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.ProjectDetailRepository;
import cn.movie.robot.dao.ProjectRepository;
import cn.movie.robot.model.Project;
import cn.movie.robot.model.ProjectDetail;
import cn.movie.robot.service.IProjectAmountService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Service
public class ProjectAmountServiceImpl implements IProjectAmountService {

  @Resource
  ProjectRepository projectRepository;

  @Resource
  ProjectDetailRepository projectDetailRepository;


  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  @Override
  public void refreshAmount(Integer projectId) {
    Project project = projectRepository.getOne(projectId);
    if (Objects.isNull(project)){
      return;
    }
    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectId(projectId);
    List<ProjectDetail> parentDetailList = projectDetailList.stream()
        .filter(projectDetail -> Objects.isNull(projectDetail.getFeeChildCategoryId()))
        .collect(Collectors.toList());

    BigDecimal budgetCost = new BigDecimal(0);
    BigDecimal realCost = new BigDecimal(0);
    BigDecimal shootingBudget = new BigDecimal(0);
    BigDecimal lateStateBudget = new BigDecimal(0);
    BigDecimal shootingCost = new BigDecimal(0);
    BigDecimal lateStateCost = new BigDecimal(0);

    for (ProjectDetail projectDetail : parentDetailList){
      realCost = realCost.add(projectDetail.getRealAmount());
      budgetCost = budgetCost.add(projectDetail.getBudgetAmount());
      if (projectDetail.getStage().equals(Constants.PROJECT_DETAIL_STATG_SHOOTING)){
        shootingBudget = shootingBudget.add(projectDetail.getBudgetAmount());
        shootingCost = shootingCost.add(projectDetail.getRealAmount());
      }
      if (projectDetail.getStage().equals(Constants.PROJECT_DETAIL_STATG_LAST_STATE)){
        lateStateBudget = lateStateBudget.add(projectDetail.getBudgetAmount());
        lateStateCost = lateStateCost.add(projectDetail.getRealAmount());
      }
    }
    project.setRealCost(realCost);
    project.setBudgetCost(budgetCost);
    project.setShootingBudget(shootingBudget);
    project.setShootingCost(shootingCost);
    project.setLateStateBudget(lateStateBudget);
    project.setLateStateCost(lateStateCost);
    projectRepository.save(project);
  }
}
