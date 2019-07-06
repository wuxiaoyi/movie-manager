package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.ProjectMemberRepository;
import cn.movie.robot.dao.ProjectRepository;
import cn.movie.robot.enums.ProjectMemberTypeEnum;
import cn.movie.robot.model.Project;
import cn.movie.robot.model.ProjectMember;
import cn.movie.robot.model.User;
import cn.movie.robot.service.IProjectSearchService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.search.ProjectSearchVo;
import cn.movie.robot.vo.resp.PageBean;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
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

  @Override
  public Result search(ProjectSearchVo projectSearchVo) {
    List<Integer> projectIds = null;
    List<Integer> memberProjectIds = queryProjectIdByMember(projectSearchVo);
    if (Objects.nonNull(memberProjectIds)){
      if (memberProjectIds.size() == 0){
        return emptyResult();
      }
      projectIds = new ArrayList<>();
      projectIds.addAll(memberProjectIds);
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
