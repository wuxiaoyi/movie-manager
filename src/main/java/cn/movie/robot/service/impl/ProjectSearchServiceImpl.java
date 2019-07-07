package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.ProjectDetailRepository;
import cn.movie.robot.dao.ProjectMemberRepository;
import cn.movie.robot.dao.ProjectRepository;
import cn.movie.robot.enums.ProjectMemberTypeEnum;
import cn.movie.robot.model.Project;
import cn.movie.robot.model.ProjectDetail;
import cn.movie.robot.model.ProjectMember;
import cn.movie.robot.service.IProjectSearchService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.search.FeeSearchVo;
import cn.movie.robot.vo.req.search.ProjectSearchVo;
import cn.movie.robot.vo.resp.PageBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Service
public class ProjectSearchServiceImpl implements IProjectSearchService {

  @Resource
  ProjectMemberRepository projectMemberRepository;

  @Resource
  ProjectRepository projectRepository;

  @Resource
  ProjectDetailRepository projectDetailRepository;

  @Override
  public Result search(ProjectSearchVo projectSearchVo) {
    List<Integer> projectIds = null;

    // 处理项目成员搜索
    List<Integer> memberProjectIds = queryProjectIdByMember(projectSearchVo);
    if (Objects.nonNull(memberProjectIds)){
      if (memberProjectIds.size() == 0){
        return emptyResult();
      }
      projectIds = new ArrayList<>();
      projectIds.addAll(memberProjectIds);
    }

    // 处理费用项搜索
    List<Integer> feeProjectIds = queryProjectIdByFee(projectSearchVo);
    if (Objects.nonNull(feeProjectIds)){
      if (feeProjectIds.size() == 0){
        return emptyResult();
      }
      if (Objects.isNull(projectIds)){
        projectIds = new ArrayList<>();
      }
      projectIds.addAll(feeProjectIds);
    }

    Specification<Project> specification = buildBaseQuery(projectSearchVo, projectIds);
    Pageable pageable = PageRequest.of(projectSearchVo.getPage()-1, projectSearchVo.getPageSize(), Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));

    Page<Project> projectPage = projectRepository.findAll(specification, pageable);
    PageBean<Project> projectPageBean = new PageBean<>(
        projectPage.getTotalElements(),
        projectPage.getTotalPages(),
        projectPage.getContent()
    );

    return Result.succ(projectPageBean);
  }

  /**
   * 构建费用项搜索
   * @param projectSearchVo
   * @return
   */

  private List<Integer> queryProjectIdByFee(ProjectSearchVo projectSearchVo){
    List<FeeSearchVo> feeList = projectSearchVo.getFeeList();

    if (CollectionUtils.isEmpty(feeList)){
      return null;
    }

    List<ProjectDetail> parentDetails = new ArrayList<>();
    List<ProjectDetail> childDetails = new ArrayList<>();
    boolean searchParentCategory = false;
    boolean searchChildCategory = false;

    List<Integer> parentFeeCatogoryIds = feeList.stream()
        .filter(feeSearchVo -> feeSearchVo.getCategoryType() == Constants.FEE_CATEGORY_TYPE_PARENT)
        .map(FeeSearchVo::getCategoryId).collect(Collectors.toList());

    if (parentFeeCatogoryIds.size() > 0){
      searchParentCategory = true;
      parentDetails = projectDetailRepository.queryByFeeCategoryIdInAndRealAmountGreaterThanAndFeeChildCategoryIdIsNull(
          parentFeeCatogoryIds,
          BigDecimal.ZERO
      );
    }

    List<List<Integer>> childFeeCategoryIdsList = feeList.stream()
        .filter(feeSearchVo -> feeSearchVo.getCategoryType() == Constants.FEE_CATEGORY_TYPE_CHILD)
        .map(FeeSearchVo::getChildFeeCategoryList).collect(Collectors.toList());
    List<Integer> childFeeCategoryIds = new ArrayList<>();
    for (List<Integer> ids : childFeeCategoryIdsList){
      childFeeCategoryIds.addAll(ids);
    }
    if (childFeeCategoryIds.size() > 0){
      searchChildCategory = true;
      childDetails = projectDetailRepository.queryByFeeChildCategoryIdInAndRealAmountGreaterThan(
          childFeeCategoryIds,
          BigDecimal.ZERO
      );
    }

    List<Integer> parentProjectIds = parentDetails.stream().map(ProjectDetail::getProjectId).collect(Collectors.toList());
    List<Integer> childProjectIds = childDetails.stream().map(ProjectDetail::getProjectId).collect(Collectors.toList());
    if (searchParentCategory && searchChildCategory){
      return parentProjectIds.stream().filter(item -> childProjectIds.contains(item)).collect(Collectors.toList());
    }else if (searchParentCategory){
      return parentProjectIds;
    }else if (searchChildCategory){
      return childProjectIds;
    }

    return null;
  }

  /**
   * 构建项目成员搜索
   * @param projectSearchVo
   * @return
   */
  private List<Integer> queryProjectIdByMember(ProjectSearchVo projectSearchVo){
    List<ProjectMember> producerList = new ArrayList<>();
    List<ProjectMember> directorList = new ArrayList<>();
    boolean searchProducer = false;
    boolean searchDirector = false;
    if (!CollectionUtils.isEmpty(projectSearchVo.getProducerList())){
      producerList = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.PRODUCER.getType(),
          projectSearchVo.getProducerList()
      );
      searchProducer = true;
    }
    if (!CollectionUtils.isEmpty(projectSearchVo.getDirectorList())){
      directorList = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.DIRECTOR.getType(),
          projectSearchVo.getDirectorList()
      );
      searchDirector = true;
    }

    if (searchProducer && searchDirector){
      List<Integer> producerProjectIds = producerList.stream().map(ProjectMember::getProjectId).collect(Collectors.toList());
      List<Integer> directorProjectIds = directorList.stream().map(ProjectMember::getProjectId).collect(Collectors.toList());
      return producerProjectIds.stream().filter(item -> directorProjectIds.contains(item)).collect(Collectors.toList());
    } else if (searchProducer){
      return producerList.stream().map(ProjectMember::getProjectId).collect(Collectors.toList());
    } else if (searchDirector){
      return directorList.stream().map(ProjectMember::getProjectId).collect(Collectors.toList());
    }
    return null;
  }



  /**
   * 构建项目基本信息搜索
   * @param projectSearchVo
   * @return
   */
  private Specification<Project> buildBaseQuery(ProjectSearchVo projectSearchVo, List<Integer> projectIds){
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
//      if (StringUtils.isNoneEmpty(projectSearchVo.getCompanyName())){
//        predicates.add(criteriaBuilder.like(root.<String>get("company_name"), projectSearchVo.getCompanyName()));
//      }


      if (Objects.nonNull(projectIds)){
        predicates.add(root.<Integer>get("id").in(projectIds));
      }

      /**
       * 金额start
       */
      if (Objects.nonNull(projectSearchVo.getBudgetCostStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("budgetCost"), projectSearchVo.getBudgetCostStart()));
      }
      if (Objects.nonNull(projectSearchVo.getBudgetCostEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("budgetCost"), projectSearchVo.getBudgetCostEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getRealCostStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("realCost"), projectSearchVo.getRealCostStart()));
      }
      if (Objects.nonNull(projectSearchVo.getRealCostEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("realCost"), projectSearchVo.getRealCostEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getContractAmountStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("contractAmount"), projectSearchVo.getContractAmountStart()));
      }
      if (Objects.nonNull(projectSearchVo.getContractAmountEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("contractAmount"), projectSearchVo.getContractAmountEnd()));
      }
      /**
       * 金额end
       */
      /**
       * 成片，拍摄start
       */
      if (Objects.nonNull(projectSearchVo.getFilmDurationStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("filmDuration"), projectSearchVo.getFilmDurationStart()));
      }
      if (Objects.nonNull(projectSearchVo.getFilmDurationEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("filmDuration"), projectSearchVo.getFilmDurationEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getShootingDurationStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("shootingDuration"), projectSearchVo.getShootingDurationStart()));
      }
      if (Objects.nonNull(projectSearchVo.getShootingDurationEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("shootingDuration"), projectSearchVo.getShootingDurationEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getShootingStartAtStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("shootingStartAt"), projectSearchVo.getShootingStartAtStart()));
      }
      if (Objects.nonNull(projectSearchVo.getShootingStartAtEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("shootingStartAt"), projectSearchVo.getShootingStartAtEnd()));
      }
      /**
       * 成片，拍摄end
       */

      return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
    };
  }

  private Result emptyResult(){
    PageBean<Project> projectPageBean = new PageBean<>(
        0,
        0,
        null
    );
    return Result.succ(projectPageBean);
  }
}
