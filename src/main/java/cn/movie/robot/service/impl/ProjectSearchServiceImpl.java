package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.*;
import cn.movie.robot.enums.ProjectMemberTypeEnum;
import cn.movie.robot.model.*;
import cn.movie.robot.service.IProjectSearchService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.search.FeeSearchVo;
import cn.movie.robot.vo.req.search.ProjectSearchVo;
import cn.movie.robot.vo.resp.PageBean;
import cn.movie.robot.vo.resp.search.ProjectSearchChildFeeRespVo;
import cn.movie.robot.vo.resp.search.ProjectSearchParentFeeRespVo;
import cn.movie.robot.vo.resp.search.ProjectSearchRespVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Hash;
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
@Slf4j
@Service
public class ProjectSearchServiceImpl implements IProjectSearchService {

  @Resource
  ProjectMemberRepository projectMemberRepository;

  @Resource
  ProjectRepository projectRepository;

  @Resource
  ProjectDetailRepository projectDetailRepository;

  @Resource
  ProviderRepository providerRepository;

  @Resource
  StaffRepository staffRepository;

  @Override
  public Result search(ProjectSearchVo projectSearchVo) {
    List<Integer> projectIds = null;

    // 处理项目成员搜索
    List<Integer> memberProjectIds = queryProjectIdByMember(projectSearchVo);
    if (Objects.nonNull(memberProjectIds)){
      if (memberProjectIds.size() == 0){
        return emptyResult();
      }
      if (Objects.isNull(projectIds)) {
        projectIds = new ArrayList<>();
        projectIds.addAll(memberProjectIds);
      }else {
        projectIds = projectIds.stream().filter(item -> memberProjectIds.contains(item)).collect(Collectors.toList());
      }
      logger.info("ProjectSearchServiceImpl search, merber search project ids: {}", projectIds);
    }

    // 处理费用项搜索
    List<Integer> feeProjectIds = queryProjectIdByFee(projectSearchVo);
    if (Objects.nonNull(feeProjectIds)){
      if (feeProjectIds.size() == 0){
        return emptyResult();
      }
      if (Objects.isNull(projectIds)){
        projectIds = new ArrayList<>();
        projectIds.addAll(feeProjectIds);
      }else {
        projectIds = projectIds.stream().filter(item -> feeProjectIds.contains(item)).collect(Collectors.toList());
      }
      logger.info("ProjectSearchServiceImpl search, fee search project ids: {}", projectIds);
    }

    if (Objects.nonNull(projectIds) && projectIds.size() == 0){
      return emptyResult();
    }

    Specification<Project> specification = buildBaseQuery(projectSearchVo, projectIds);
    Pageable pageable = PageRequest.of(projectSearchVo.getPage()-1, projectSearchVo.getPageSize(), Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));

    Page<Project> projectPage = projectRepository.findAll(specification, pageable);
    PageBean<ProjectSearchRespVo> projectPageBean = new PageBean<>(
        projectPage.getTotalElements(),
        projectPage.getTotalPages(),
        dealSearchResult(projectPage.getContent(), projectSearchVo)
    );
    return Result.succ(projectPageBean);
  }

  @Override
  public List<ProjectSearchRespVo> searchForExport(ProjectSearchVo projectSearchVo) {
    List<ProjectSearchRespVo> result = new ArrayList<>();
    List<Integer> projectIds = null;

    // 处理项目成员搜索
    List<Integer> memberProjectIds = queryProjectIdByMember(projectSearchVo);
    if (Objects.nonNull(memberProjectIds)){
      if (memberProjectIds.size() == 0){
        return result;
      }
      if (Objects.isNull(projectIds)) {
        projectIds = new ArrayList<>();
        projectIds.addAll(memberProjectIds);
      }else {
        projectIds = projectIds.stream().filter(item -> memberProjectIds.contains(item)).collect(Collectors.toList());
      }
    }

    // 处理费用项搜索
    List<Integer> feeProjectIds = queryProjectIdByFee(projectSearchVo);
    if (Objects.nonNull(feeProjectIds)){
      if (feeProjectIds.size() == 0){
        return result;
      }
      if (Objects.isNull(projectIds)){
        projectIds = new ArrayList<>();
        projectIds.addAll(feeProjectIds);
      }else {
        projectIds = projectIds.stream().filter(item -> feeProjectIds.contains(item)).collect(Collectors.toList());
      }
    }

    Specification<Project> specification = buildBaseQuery(projectSearchVo, projectIds);
    Pageable pageable = PageRequest.of(0, 100, Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));

    Page<Project> projectPage = projectRepository.findAll(specification, pageable);
    return dealSearchResult(projectPage.getContent(), projectSearchVo);
  }

  private List<ProjectSearchRespVo> dealSearchResult(List<Project> projects, ProjectSearchVo projectSearchVo){
    if (CollectionUtils.isEmpty(projectSearchVo.getFeeList()) && CollectionUtils.isEmpty(projectSearchVo.getProviderList())){
      return dealSearchResultWithoutFee(projects, projectSearchVo);
    }else {
      return dealSearchResultWithFee(projects, projectSearchVo);
    }
  }

  /**
   * 搜索条件不带费用项时，不用出费用项结果
   * @param projects
   * @return
   */
  private List<ProjectSearchRespVo> dealSearchResultWithoutFee(List<Project> projects, ProjectSearchVo projectSearchVo){
    List<ProjectSearchRespVo> projectSearchRespVoList = new ArrayList<>();

    List<Integer> projectIds = projects.stream().map(Project::getId).collect(Collectors.toList());
    List<ProjectMember> projectMemberList = projectMemberRepository.queryByProjectIdIn(projectIds);
    List<Integer> staffIds = projectMemberList.stream().map(ProjectMember::getStaffId).collect(Collectors.toList());
    HashMap<Integer, String> staffNameHash = genStaffNameHash(staffIds);

    for (Project project : projects) {
      ProjectSearchRespVo projectSearchRespVo = buildBaseResp(project);
      projectSearchRespVoList.add(projectSearchRespVo);
      buildMemberResp(projectSearchRespVo, projectMemberList, staffNameHash);
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

    /**
     * 过滤并查询出父费用项
     */
    List<FeeSearchVo> feeList = projectSearchVo.getFeeList();
    List<Integer> parentFeeCatogoryIds = parseParentFeeCatgoryIds(feeList);
    HashMap<Integer, List<ProjectSearchParentFeeRespVo>> parentFeeRespVoHashMap = buildParentFeeVo(projectIds, parentFeeCatogoryIds);

    /**
     * 根据子费用项和供应商过滤并查询出子费用项
     */
    List<Integer> childFeeCategoryIds = parseChildFeeCatgoryIds(feeList);
    List<Integer> providerIds = projectSearchVo.getProviderList();
    HashMap<Integer, List<ProjectSearchParentFeeRespVo>> childFeeRespVoHashMap = buildChildFeeVo(projectIds, childFeeCategoryIds, providerIds);

    List<ProjectMember> projectMemberList = projectMemberRepository.queryByProjectIdIn(projectIds);
    List<Integer> staffIds = projectMemberList.stream().map(ProjectMember::getStaffId).collect(Collectors.toList());
    HashMap<Integer, String> staffNameHash = genStaffNameHash(staffIds);

    for (Project project : projects){
      ProjectSearchRespVo projectSearchRespVo = buildBaseResp(project);
      buildMemberResp(projectSearchRespVo, projectMemberList, staffNameHash);

      List<ProjectSearchParentFeeRespVo> parentFeeList = parentFeeRespVoHashMap.get(project.getId());
      List<ProjectSearchParentFeeRespVo> childFeeList = childFeeRespVoHashMap.get(project.getId());
      if (Objects.isNull(parentFeeList)){
        parentFeeList = new ArrayList<>();
      }
      if (Objects.isNull(childFeeList)){
        childFeeList = new ArrayList<>();
      }
      // 如果没有选择一级搜索项，才放入二级搜索项结果，否则页面只需要展示一级费用
      if (CollectionUtils.isEmpty(parentFeeCatogoryIds)){
        parentFeeList.addAll(childFeeList);
      }
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
    List<Integer> projectIds = null;

    List<FeeSearchVo> feeList = projectSearchVo.getFeeList();
    List<Integer> providerIds = projectSearchVo.getProviderList();

    if (CollectionUtils.isEmpty(feeList) && CollectionUtils.isEmpty(providerIds)){
      return projectIds;
    }

    List<Integer> parentFeeCatogoryIds = parseParentFeeCatgoryIds(feeList);

    if (parentFeeCatogoryIds.size() > 0){
      List<ProjectDetail> parentDetails = projectDetailRepository.queryByFeeCategoryIdInAndRealAmountGreaterThanAndFeeChildCategoryIdIsNull(
          parentFeeCatogoryIds,
          BigDecimal.ZERO
      );
      projectIds = mergeProjectId(projectIds, parentDetails.stream().map(ProjectDetail::getProjectId).collect(Collectors.toList()));
    }

    List<Integer> childFeeCategoryIds = parseChildFeeCatgoryIds(feeList);
    if (childFeeCategoryIds.size() > 0){
      List<ProjectDetail> childDetails = projectDetailRepository.queryByFeeChildCategoryIdInAndRealAmountGreaterThan(
          childFeeCategoryIds,
          BigDecimal.ZERO
      );
      projectIds = mergeProjectId(projectIds, childDetails.stream().map(ProjectDetail::getProjectId).collect(Collectors.toList()));
    }

    if (!CollectionUtils.isEmpty(providerIds)){
      List<ProjectDetail> providerDetails = projectDetailRepository.queryByProviderIdIn(providerIds);
      projectIds = mergeProjectId(projectIds, providerDetails.stream().map(ProjectDetail::getProjectId).collect(Collectors.toList()));
    }

    return projectIds;
  }

  /**
   * 构建项目成员搜索
   * @param projectSearchVo
   * @return
   */
  private List<Integer> queryProjectIdByMember(ProjectSearchVo projectSearchVo){
    List<Integer> projectIds = null;

    if (!CollectionUtils.isEmpty(projectSearchVo.getProducerList())){
      List<ProjectMember> members = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.PRODUCER.getType(),
          projectSearchVo.getProducerList()
      );
      projectIds = mergeProjectId(projectIds, members.stream().map(ProjectMember::getProjectId).collect(Collectors.toList()));
    }
    if (!CollectionUtils.isEmpty(projectSearchVo.getDirectorList())){
      List<ProjectMember> members = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.DIRECTOR.getType(),
          projectSearchVo.getDirectorList()
      );
      projectIds = mergeProjectId(projectIds, members.stream().map(ProjectMember::getProjectId).collect(Collectors.toList()));
    }
    if (!CollectionUtils.isEmpty(projectSearchVo.getProjectLeaderList())){
      List<ProjectMember> members = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.PROJECT_LEADER.getType(),
          projectSearchVo.getProjectLeaderList()
      );
      projectIds = mergeProjectId(projectIds, members.stream().map(ProjectMember::getProjectId).collect(Collectors.toList()));
    }
    if (!CollectionUtils.isEmpty(projectSearchVo.getCustomerManagerList())){
      List<ProjectMember> members = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.CUSTOMER_MANAGER.getType(),
          projectSearchVo.getCustomerManagerList()
      );
      projectIds = mergeProjectId(projectIds, members.stream().map(ProjectMember::getProjectId).collect(Collectors.toList()));
    }
    if (!CollectionUtils.isEmpty(projectSearchVo.getArtList())){
      List<ProjectMember> members = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.ART.getType(),
          projectSearchVo.getArtList()
      );
      projectIds = mergeProjectId(projectIds, members.stream().map(ProjectMember::getProjectId).collect(Collectors.toList()));
    }
    if (!CollectionUtils.isEmpty(projectSearchVo.getCompositingList())){
      List<ProjectMember> members = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.COMPOSITING.getType(),
          projectSearchVo.getCompositingList()
      );
      projectIds = mergeProjectId(projectIds, members.stream().map(ProjectMember::getProjectId).collect(Collectors.toList()));
    }
    if (!CollectionUtils.isEmpty(projectSearchVo.getCopyWritingList())){
      List<ProjectMember> members = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.COPYWRITING.getType(),
          projectSearchVo.getCopyWritingList()
      );
      projectIds = mergeProjectId(projectIds, members.stream().map(ProjectMember::getProjectId).collect(Collectors.toList()));
    }
    if (!CollectionUtils.isEmpty(projectSearchVo.getExecutiveDirecrotList())){
      List<ProjectMember> members = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.EXECUTIVE_DIRECTOR.getType(),
          projectSearchVo.getExecutiveDirecrotList()
      );
      projectIds = mergeProjectId(projectIds, members.stream().map(ProjectMember::getProjectId).collect(Collectors.toList()));
    }
    if (!CollectionUtils.isEmpty(projectSearchVo.getMusicList())){
      List<ProjectMember> members = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.MUSIC.getType(),
          projectSearchVo.getMusicList()
      );
      projectIds = mergeProjectId(projectIds, members.stream().map(ProjectMember::getProjectId).collect(Collectors.toList()));
    }
    if (!CollectionUtils.isEmpty(projectSearchVo.getPostEditingList())){
      List<ProjectMember> members = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.POST_EDITING.getType(),
          projectSearchVo.getPostEditingList()
      );
      projectIds = mergeProjectId(projectIds, members.stream().map(ProjectMember::getProjectId).collect(Collectors.toList()));
    }
    if (!CollectionUtils.isEmpty(projectSearchVo.getStoryBoardList())){
      List<ProjectMember> members = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.STORY_BOARD.getType(),
          projectSearchVo.getStoryBoardList()
      );
      projectIds = mergeProjectId(projectIds, members.stream().map(ProjectMember::getProjectId).collect(Collectors.toList()));
    }
    return projectIds;
  }



  /**
   * 构建项目基本信息搜索
   * @param projectSearchVo
   * @return
   */
  private Specification<Project> buildBaseQuery(ProjectSearchVo projectSearchVo, List<Integer> projectIds){
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (!CollectionUtils.isEmpty(projectIds)){
        predicates.add(root.<Integer>get("id").in(projectIds));
      }

      if (Objects.nonNull(projectSearchVo.getCompanyId())){
        predicates.add(criteriaBuilder.equal(root.get("companyId"), projectSearchVo.getCompanyId()));
      }

      if (Objects.nonNull(projectSearchVo.getChildCompanyId())){
        predicates.add(criteriaBuilder.equal(root.get("childCompanyId"), projectSearchVo.getChildCompanyId()));
      }

      if (StringUtils.isNoneEmpty(projectSearchVo.getName())){
        predicates.add(criteriaBuilder.like(root.get("name"), "%" + projectSearchVo.getName() + "%"));
      }

      if (StringUtils.isNoneEmpty(projectSearchVo.getSid())){
        predicates.add(criteriaBuilder.equal(root.get("sid"), projectSearchVo.getSid()));
      }

      if (!CollectionUtils.isEmpty(projectSearchVo.getStates())){
        predicates.add(root.<Integer>get("state").in(projectSearchVo.getStates()));
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

      if (Objects.nonNull(projectSearchVo.getReturnAmountStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("returnAmount"), projectSearchVo.getReturnAmountStart()));
      }
      if (Objects.nonNull(projectSearchVo.getReturnAmountEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("returnAmount"), projectSearchVo.getReturnAmountEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getShootingBudgetCostStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("shootingBudget"), projectSearchVo.getShootingBudgetCostStart()));
      }
      if (Objects.nonNull(projectSearchVo.getShootingBudgetCostEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("shootingBudget"), projectSearchVo.getShootingBudgetCostEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getLateStateBudgetCostStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("lateStateBudget"), projectSearchVo.getLateStateBudgetCostStart()));
      }
      if (Objects.nonNull(projectSearchVo.getLateStateBudgetCostEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("lateStateBudget"), projectSearchVo.getLateStateBudgetCostEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getShootingRealCostStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("shootingCost"), projectSearchVo.getShootingRealCostStart()));
      }
      if (Objects.nonNull(projectSearchVo.getShootingRealCostEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("shootingCost"), projectSearchVo.getShootingRealCostEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getLateStateRealCostStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("lateStateCost"), projectSearchVo.getLateStateBudgetCostStart()));
      }
      if (Objects.nonNull(projectSearchVo.getLateStateRealCostEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("lateStateCost"), projectSearchVo.getLateStateBudgetCostEnd()));
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
  private HashMap<Integer, List<ProjectSearchParentFeeRespVo>> buildChildFeeVo(List<Integer> projectIds, List<Integer> feeCategoryIds, List<Integer> providerIds){
    HashMap<Integer, List<ProjectSearchParentFeeRespVo>> result = new HashMap<>(feeCategoryIds.size());

//    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdInAndFeeChildCategoryIdIn(projectIds, feeCategoryIds);
    List<ProjectDetail> projectDetailList = projectDetailRepository.queryByProjectIdIn(projectIds);

    if (!CollectionUtils.isEmpty(feeCategoryIds)){
      projectDetailList = projectDetailList.stream().filter(projectDetail -> feeCategoryIds.contains(projectDetail.getFeeChildCategoryId())).collect(Collectors.toList());
    }
    if (!CollectionUtils.isEmpty(providerIds)){
      projectDetailList = projectDetailList.stream().filter(projectDetail -> providerIds.contains(projectDetail.getProviderId())).collect(Collectors.toList());
    }

    if (projectDetailList.size() == 0){
      return result;
    }
    HashMap<Integer, String> providerNameHash = genProviderNameHash(
      projectDetailList.stream().map(ProjectDetail::getProviderId).collect(Collectors.toList())
    );

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
        childFeeRespVo.setProviderName(providerNameHash.get(childFeeDetail.getProviderId()));
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

  private HashMap<Integer, String> genProviderNameHash(List<Integer> providerIds){
    HashMap<Integer, String> nameHash = new HashMap<>();
    if (providerIds.size() == 0){
      return nameHash;
    }
    List<Provider> providers = providerRepository.queryByIdIn(providerIds);
    for (Provider provider : providers){
      nameHash.put(provider.getId(), provider.getName());
    }
    return nameHash;
  }

  private HashMap<Integer, String> genStaffNameHash(List<Integer> staffIds){
    HashMap<Integer, String> nameHash = new HashMap<>();
    if (staffIds.size() == 0){
      return nameHash;
    }
    List<Staff> staffs = staffRepository.queryByIdIn(staffIds);
    for (Staff staff : staffs){
      nameHash.put(staff.getId(), staff.getName());
    }
    return nameHash;
  }

  /**
   * 取项目id交集
   * @param projectIds
   * @param newProjectIds
   * @return
   */
  private List<Integer> mergeProjectId(List<Integer> projectIds, List<Integer> newProjectIds){
    if (Objects.isNull(projectIds)){
      return newProjectIds;
    }
    return projectIds.stream().filter(item -> newProjectIds.contains(item)).collect(Collectors.toList());
  }

  private ProjectSearchRespVo buildBaseResp(Project project){
    ProjectSearchRespVo projectSearchRespVo = new ProjectSearchRespVo();
    projectSearchRespVo.setId(project.getId());
    projectSearchRespVo.setName(project.getName());
    projectSearchRespVo.setSid(project.getSid());
    projectSearchRespVo.setState(project.getState());
    projectSearchRespVo.setContractSubjectId(project.getContractSubjectId());
    projectSearchRespVo.setCompanyId(project.getCompanyId());
    projectSearchRespVo.setChildCompanyId(project.getChildCompanyId());
    projectSearchRespVo.setContractAmount(project.getContractAmount());
    projectSearchRespVo.setRealCost(project.getRealCost());
    projectSearchRespVo.setBudgetCost(project.getBudgetCost());
    projectSearchRespVo.setReturnAmount(project.getReturnAmount());
    projectSearchRespVo.setLateStateBudget(project.getLateStateBudget());
    projectSearchRespVo.setLateStateCost(project.getLateStateCost());
    projectSearchRespVo.setShootingBudget(project.getShootingBudget());
    projectSearchRespVo.setShootingCost(project.getShootingCost());
    projectSearchRespVo.setFilmDuration(filmDuration(project.getFilmDuration()));
    projectSearchRespVo.setShootingDuration(shootingDuration(project.getShootingDuration()));
    projectSearchRespVo.setShootingStartAt(project.getShootingStartAt());
    return projectSearchRespVo;
  }

  private void buildMemberResp(ProjectSearchRespVo projectSearchRespVo, List<ProjectMember> projectMemberList, HashMap<Integer, String> staffNameHash){
    List<String> projectLeaderList = new ArrayList<>();
    List<String> customerManagerList = new ArrayList<>();
    List<String> executiveDirecrotList = new ArrayList<>();
    List<String> copyWritingList = new ArrayList<>();
    List<String> postEditingList = new ArrayList<>();
    List<String> compositingList = new ArrayList<>();
    List<String> artList = new ArrayList<>();
    List<String> musicList = new ArrayList<>();
    List<String> storyBoardList = new ArrayList<>();
    List<String> directorList = new ArrayList<>();
    List<String> producerList = new ArrayList<>();
    for (ProjectMember projectMember : projectMemberList){
      if (projectMember.getProjectId().equals(projectSearchRespVo.getId())){
        if (projectMember.getMemberType().equals(ProjectMemberTypeEnum.PROJECT_LEADER.getType())){
          projectLeaderList.add(staffNameHash.get(projectMember.getStaffId()));
        }else if (projectMember.getMemberType().equals(ProjectMemberTypeEnum.CUSTOMER_MANAGER.getType())){
          customerManagerList.add(staffNameHash.get(projectMember.getStaffId()));
        }else if (projectMember.getMemberType().equals(ProjectMemberTypeEnum.DIRECTOR.getType())){
          directorList.add(staffNameHash.get(projectMember.getStaffId()));
        }else if (projectMember.getMemberType().equals(ProjectMemberTypeEnum.EXECUTIVE_DIRECTOR.getType())){
          executiveDirecrotList.add(staffNameHash.get(projectMember.getStaffId()));
        }else if (projectMember.getMemberType().equals(ProjectMemberTypeEnum.COPYWRITING.getType())){
          copyWritingList.add(staffNameHash.get(projectMember.getStaffId()));
        }else if (projectMember.getMemberType().equals(ProjectMemberTypeEnum.PRODUCER.getType())){
          producerList.add(staffNameHash.get(projectMember.getStaffId()));
        }else if (projectMember.getMemberType().equals(ProjectMemberTypeEnum.POST_EDITING.getType())){
          postEditingList.add(staffNameHash.get(projectMember.getStaffId()));
        }else if (projectMember.getMemberType().equals(ProjectMemberTypeEnum.COMPOSITING.getType())){
          compositingList.add(staffNameHash.get(projectMember.getStaffId()));
        }else if (projectMember.getMemberType().equals(ProjectMemberTypeEnum.ART.getType())){
          artList.add(staffNameHash.get(projectMember.getStaffId()));
        }else if (projectMember.getMemberType().equals(ProjectMemberTypeEnum.MUSIC.getType())){
          musicList.add(staffNameHash.get(projectMember.getStaffId()));
        }else if (projectMember.getMemberType().equals(ProjectMemberTypeEnum.STORY_BOARD.getType())){
          storyBoardList.add(staffNameHash.get(projectMember.getStaffId()));
        }
      }
    }
    projectSearchRespVo.setProjectLeaderList(String.join(",", projectLeaderList));
    projectSearchRespVo.setCustomerManagerList(String.join(",", customerManagerList));
    projectSearchRespVo.setDirectorList(String.join(",", directorList));
    projectSearchRespVo.setExecutiveDirecrotList(String.join(",", executiveDirecrotList));
    projectSearchRespVo.setCopyWritingList(String.join(",", copyWritingList));
    projectSearchRespVo.setProducerList(String.join(",", producerList));
    projectSearchRespVo.setPostEditingList(String.join(",", postEditingList));
    projectSearchRespVo.setCompositingList(String.join(",", compositingList));
    projectSearchRespVo.setArtList(String.join(",", artList));
    projectSearchRespVo.setMusicList(String.join(",", musicList));
    projectSearchRespVo.setStoryBoardList(String.join(",", storyBoardList));
  }

  private String filmDuration(Integer duration){
    if (Objects.isNull(duration)){
      return "";
    }
    int hour = duration / 60;
    int minute = duration % 60;
    String result = "";
    if (hour != 0){
      result += hour + "小时";
    }
    if (minute != 0){
      result += minute + "分钟";
    }
    return result;
  }

  private String shootingDuration(Integer duration){
    if (Objects.isNull(duration)){
      return "";
    }
    return duration.toString() + "天";
  }
}
