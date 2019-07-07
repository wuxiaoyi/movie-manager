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
import cn.movie.robot.vo.resp.search.ProjectSearchChildFeeRespVo;
import cn.movie.robot.vo.resp.search.ProjectSearchParentFeeRespVo;
import cn.movie.robot.vo.resp.search.ProjectSearchRespVo;
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
    PageBean<ProjectSearchRespVo> projectPageBean = new PageBean<>(
        projectPage.getTotalElements(),
        projectPage.getTotalPages(),
        dealSearchResultWithoutFee(projectPage.getContent(), projectSearchVo)
    );
    return Result.succ(projectPageBean);
  }

  private List<ProjectSearchRespVo> dealSearchResultWithoutFee(List<Project> projects, ProjectSearchVo projectSearchVo){
    if (CollectionUtils.isEmpty(projectSearchVo.getFeeList())){
      return dealSearchResultWithoutFee(projects);
    }else {
      return dealSearchResultWithFee(projects, projectSearchVo);
    }
  }

  /**
   * 搜索条件不带费用项时，不用出费用项结果
   * @param projects
   * @return
   */
  private List<ProjectSearchRespVo> dealSearchResultWithoutFee(List<Project> projects){
    List<ProjectSearchRespVo> projectSearchRespVoList = new ArrayList<>();
    for (Project project : projects) {
      ProjectSearchRespVo projectSearchRespVo = new ProjectSearchRespVo();
      projectSearchRespVo.setId(project.getId());
      projectSearchRespVo.setName(project.getName());
      projectSearchRespVo.setSid(project.getSid());
      projectSearchRespVoList.add(projectSearchRespVo);
    }
    return projectSearchRespVoList;
  }

  /**
   * 搜索条件带费用项时，需要出费用项结果
   * @param projects
   * @param projectSearchVo
   * @return
   */
  private List<ProjectSearchRespVo> dealSearchResultWithFee(List<Project> projects, ProjectSearchVo projectSearchVo){
    List<ProjectSearchRespVo> projectSearchRespVoList = new ArrayList<>();
    List<Integer> projectIds = projects.stream().map(Project::getId).collect(Collectors.toList());

    List<FeeSearchVo> feeList = projectSearchVo.getFeeList();
    List<Integer> parentFeeCatogoryIds = parseParentFeeCatgoryIds(feeList);
    HashMap<Integer, List<ProjectSearchParentFeeRespVo>> parentFeeRespVoHashMap = buildParentFeeVo(projectIds, parentFeeCatogoryIds);
    List<Integer> childFeeCatogoryIds = parseChildFeeCatgoryIds(feeList);
    HashMap<Integer, List<ProjectSearchParentFeeRespVo>> childFeeRespVoHashMap = buildChildFeeVo(projectIds, childFeeCatogoryIds);


    for (Project project : projects){
      ProjectSearchRespVo projectSearchRespVo = new ProjectSearchRespVo();
      projectSearchRespVo.setId(project.getId());
      projectSearchRespVo.setName(project.getName());
      projectSearchRespVo.setSid(project.getSid());
      List<ProjectSearchParentFeeRespVo> parentFeeList = parentFeeRespVoHashMap.get(project.getId());
      List<ProjectSearchParentFeeRespVo> childFeeList = childFeeRespVoHashMap.get(project.getId());
      if (Objects.isNull(parentFeeList)){
        parentFeeList = new ArrayList<>();
      }
      if (Objects.isNull(childFeeList)){
        childFeeList = new ArrayList<>();
      }
      parentFeeList.addAll(childFeeList);
      projectSearchRespVo.setProjectDetailList(parentFeeList);
      projectSearchRespVoList.add(projectSearchRespVo);
    }

    return projectSearchRespVoList;
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

    List<Integer> parentFeeCatogoryIds = parseParentFeeCatgoryIds(feeList);

    if (parentFeeCatogoryIds.size() > 0){
      searchParentCategory = true;
      parentDetails = projectDetailRepository.queryByFeeCategoryIdInAndRealAmountGreaterThanAndFeeChildCategoryIdIsNull(
          parentFeeCatogoryIds,
          BigDecimal.ZERO
      );
    }

    List<Integer> childFeeCategoryIds = parseChildFeeCatgoryIds(feeList);
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

  private List<Integer> parseParentFeeCatgoryIds(List<FeeSearchVo> feeList){
    return feeList.stream()
        .filter(feeSearchVo -> feeSearchVo.getCategoryType() == Constants.FEE_CATEGORY_TYPE_PARENT)
        .map(FeeSearchVo::getCategoryId).collect(Collectors.toList());

  }

  private List<Integer> parseChildFeeCatgoryIds(List<FeeSearchVo> feeList){
    List<List<Integer>> childFeeCategoryIdsList = feeList.stream()
        .filter(feeSearchVo -> feeSearchVo.getCategoryType() == Constants.FEE_CATEGORY_TYPE_CHILD)
        .map(FeeSearchVo::getChildFeeCategoryList).collect(Collectors.toList());
    List<Integer> childFeeCategoryIds = new ArrayList<>();
    for (List<Integer> ids : childFeeCategoryIdsList){
      childFeeCategoryIds.addAll(ids);
    }
    return childFeeCategoryIds;
  }

  /**
   * 根据一级费用id构建出所有项目的一级费用项结果，减少数据库查询
   * @param feeCategoryIds
   * @return
   */
  private HashMap<Integer, List<ProjectSearchParentFeeRespVo>> buildParentFeeVo(List<Integer> projectIds, List<Integer> feeCategoryIds){
    HashMap<Integer, List<ProjectSearchParentFeeRespVo>> result = new HashMap<>(feeCategoryIds.size());
    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdInAndFeeCategoryIdInAndFeeChildCategoryIdIsNull(
        projectIds,
        feeCategoryIds
    );
    if (projectDetailList.size() == 0){
      return result;
    }

    for (ProjectDetail projectDetail : projectDetailList){
      ProjectSearchParentFeeRespVo parentFeeRespVo = new ProjectSearchParentFeeRespVo();
      parentFeeRespVo.setId(projectDetail.getId());
      parentFeeRespVo.setRealAmount(projectDetail.getRealAmount());
      parentFeeRespVo.setBudgetAmount(projectDetail.getBudgetAmount());
      parentFeeRespVo.setCategoryId(projectDetail.getFeeCategoryId());

      /**
       * 一级费用项搜索结果，只显示一级费用，构建一个空的二级费用便于前端处理
       */
      List<ProjectSearchChildFeeRespVo> childFeeList = new ArrayList<>();
      ProjectSearchChildFeeRespVo emptyVo = new ProjectSearchChildFeeRespVo();
      emptyVo.setId(0);
      childFeeList.add(emptyVo);
      parentFeeRespVo.setChildFeeList(childFeeList);

      List<ProjectSearchParentFeeRespVo> feeRespVos = result.get(projectDetail.getProjectId());
      if (Objects.isNull(feeRespVos)){
        feeRespVos = new ArrayList<>();
      }
      feeRespVos.add(parentFeeRespVo);
      result.put(projectDetail.getProjectId(), feeRespVos);
    }
    return result;
  }

  /**
   * 根据二级费用id构建出所有项目的二级费用项结果，减少数据库查询
   * 二级费用项也要带上一级费用项总和
   * @param feeCategoryIds
   * @return
   */
  private HashMap<Integer, List<ProjectSearchParentFeeRespVo>> buildChildFeeVo(List<Integer> projectIds, List<Integer> feeCategoryIds){
    HashMap<Integer, List<ProjectSearchParentFeeRespVo>> result = new HashMap<>(feeCategoryIds.size());
    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdInAndFeeChildCategoryIdIn(projectIds, feeCategoryIds);
    if (projectDetailList.size() == 0){
      return result;
    }

    List<ProjectDetail> projectDetailParentList = projectDetailRepository.queryByProjectIdInAndFeeCategoryIdInAndFeeChildCategoryIdIsNull(
        projectIds,
        projectDetailList.stream().map(ProjectDetail::getFeeCategoryId).collect(Collectors.toList())
    );

    for (ProjectDetail parentFeeDetail : projectDetailParentList){
      ProjectSearchParentFeeRespVo parentFeeRespVo = new ProjectSearchParentFeeRespVo();
      parentFeeRespVo.setId(parentFeeDetail.getId());
      parentFeeRespVo.setRealAmount(parentFeeDetail.getRealAmount());
      parentFeeRespVo.setBudgetAmount(parentFeeDetail.getBudgetAmount());
      parentFeeRespVo.setCategoryId(parentFeeDetail.getFeeCategoryId());

      List<ProjectSearchChildFeeRespVo> childFeeList = new ArrayList<>();
      List<ProjectDetail> projectDetailChildList = projectDetailList.stream()
          .filter(projectDetail ->
              projectDetail.getFeeCategoryId().equals(parentFeeDetail.getFeeCategoryId()) && projectDetail.getProjectId().equals(parentFeeDetail.getProjectId()))
          .collect(Collectors.toList());

      for (ProjectDetail childFeeDetail : projectDetailChildList){
        ProjectSearchChildFeeRespVo childFeeRespVo = new ProjectSearchChildFeeRespVo();
        childFeeRespVo.setId(childFeeDetail.getId());
        childFeeRespVo.setRealAmount(childFeeDetail.getRealAmount());
        childFeeRespVo.setBudgetAmount(childFeeDetail.getBudgetAmount());
        childFeeRespVo.setCategoryId(childFeeDetail.getFeeChildCategoryId());
        childFeeRespVo.setProviderId(childFeeDetail.getProviderId());
        childFeeList.add(childFeeRespVo);
      }
      parentFeeRespVo.setChildFeeList(childFeeList);
      List<ProjectSearchParentFeeRespVo> feeRespVos = result.get(parentFeeDetail.getProjectId());
      if (Objects.isNull(feeRespVos)){
        feeRespVos = new ArrayList<>();
      }
      feeRespVos.add(parentFeeRespVo);
      result.put(parentFeeDetail.getProjectId(), feeRespVos);
    }

    return result;
  }
}
