package cn.movie.robot.service.impl;

import cn.movie.robot.dao.ProjectDetailRepository;
import cn.movie.robot.model.ProjectDetail;
import cn.movie.robot.service.ITaskService;
import org.springframework.stereotype.Service;

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
}
