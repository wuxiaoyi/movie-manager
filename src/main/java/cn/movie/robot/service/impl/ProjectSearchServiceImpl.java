package cn.movie.robot.service.impl;

import cn.movie.robot.dao.ProjectMemberRepository;
import cn.movie.robot.enums.ProjectMemberTypeEnum;
import cn.movie.robot.model.Project;
import cn.movie.robot.model.ProjectMember;
import cn.movie.robot.service.IProjectSearchService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.search.ProjectSearchVo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
public class ProjectSearchServiceImpl implements IProjectSearchService {

  @Resource
  ProjectMemberRepository projectMemberRepository;

  @Override
  public Result search(ProjectSearchVo projectSearchVo) {
//    Specification<House> specification = this.buildSpecification(housesReq);
//    Page<House> housePage = houseDao.findAll(specification, pageable);

    List<Integer> projectIds = new ArrayList<>();
    List<Integer> memberProjectIds = queryProjectIdByMember(projectSearchVo);
    if (Objects.nonNull(memberProjectIds)){
      projectIds.addAll(memberProjectIds);
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
    if (projectSearchVo.getProducerList().size() > 0){
      producerList = projectMemberRepository.queryByMemberTypeAndStaffIdIn(
          ProjectMemberTypeEnum.PRODUCER.getType(),
          projectSearchVo.getProducerList()
      );
      searchProducer = true;
    }
    if (projectSearchVo.getDirectorList().size() > 0){
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

      /**
       * 金额start
       */
      if (Objects.nonNull(projectSearchVo.getBudgetCostStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<BigDecimal>get("budget_cost"), projectSearchVo.getBudgetCostStart()));
      }
      if (Objects.nonNull(projectSearchVo.getBudgetCostEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<BigDecimal>get("budget_cost"), projectSearchVo.getBudgetCostEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getRealCostStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<BigDecimal>get("real_cost"), projectSearchVo.getRealCostStart()));
      }
      if (Objects.nonNull(projectSearchVo.getRealCostEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<BigDecimal>get("real_cost"), projectSearchVo.getRealCostEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getContractAmountStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<BigDecimal>get("contract_amount"), projectSearchVo.getContractAmountStart()));
      }
      if (Objects.nonNull(projectSearchVo.getContractAmountEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<BigDecimal>get("contract_amount"), projectSearchVo.getContractAmountEnd()));
      }
      /**
       * 金额end
       */
      /**
       * 成片，拍摄start
       */
      if (Objects.nonNull(projectSearchVo.getFilmDurationStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Integer>get("film_duration"), projectSearchVo.getFilmDurationStart()));
      }
      if (Objects.nonNull(projectSearchVo.getFilmDurationEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Integer>get("film_duration"), projectSearchVo.getFilmDurationEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getShootingDurationStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Integer>get("shooting_duration"), projectSearchVo.getShootingDurationStart()));
      }
      if (Objects.nonNull(projectSearchVo.getShootingDurationEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Integer>get("shooting_duration"), projectSearchVo.getShootingDurationEnd()));
      }

      if (Objects.nonNull(projectSearchVo.getShootingStartAtStart())){
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("shooting_duration"), projectSearchVo.getShootingStartAtStart()));
      }
      if (Objects.nonNull(projectSearchVo.getShootingStartAtEnd())){
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("shooting_duration"), projectSearchVo.getShootingStartAtEnd()));
      }
      /**
       * 成片，拍摄end
       */

      return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
    };
  }
}
