package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.ProviderRepository;
import cn.movie.robot.model.Provider;
import cn.movie.robot.model.Staff;
import cn.movie.robot.service.IProviderService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ProviderSearchVo;
import cn.movie.robot.vo.req.ProviderVo;
import cn.movie.robot.vo.req.StaffSearchVo;
import cn.movie.robot.vo.resp.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@Service
public class ProviderServiceImpl implements IProviderService {

  @Resource
  ProviderRepository providerRepository;

  @Override
  public Result queryAll(Pageable pageable) {
    Page<Provider> providerPage = providerRepository.findAll(pageable);
    PageBean<Provider> providerPageBean = new PageBean<>(
        providerPage.getTotalElements(),
        providerPage.getTotalPages(),
        providerPage.getContent()
    );
    return Result.succ(providerPageBean);
  }

  @Override
  public Result search(ProviderSearchVo providerSearchVo) {
    Pageable pageable = PageRequest.of(providerSearchVo.getPage() - 1, providerSearchVo.getPageSize(), Sort.by(DESC, Constants.COMMON_FIELD_NAME_ID));
    Specification<Provider> specification = buildBaseQuery(providerSearchVo);

    Page<Provider> providerPage = providerRepository.findAll(specification, pageable);
    PageBean<Provider> providerPageBean = new PageBean<>(
        providerPage.getTotalElements(),
        providerPage.getTotalPages(),
        providerPage.getContent()
    );
    return Result.succ(providerPageBean);
  }

  @Override
  public Result queryAll() {
    List<Provider> providers = providerRepository.findAll();
    return Result.succ(providers);
  }

  @Override
  public Result updateState(Integer providerId, int state) {
    Provider provider = providerRepository.getOne(providerId);
    if (Objects.isNull(provider)){
      return Result.error("该供应商不存在");
    }
    provider.setState(state);
    providerRepository.save(provider);
    return Result.succ();
  }

  @Override
  public Result save(ProviderVo providerVo) {
    Provider existProvider = providerRepository.findByName(providerVo.getName());
    if (Objects.nonNull(existProvider)){
      return Result.error("该供应商名称已存在");
    }
    Provider provider = new Provider();
    provider.setName(providerVo.getName());
    provider.setBankName(providerVo.getBankName());
    provider.setBankAccount(providerVo.getBankAccount());
    provider.setCellphone(providerVo.getCellphone());
    providerRepository.save(provider);
    return Result.succ();
  }

  @Override
  public Result update(Integer providerId, ProviderVo providerVo) {
    Provider provider = providerRepository.getOne(providerId);
    if (Objects.isNull(provider)){
      return Result.error("该供应商不存在");
    }
    provider.setName(providerVo.getName());
    provider.setBankName(providerVo.getBankName());
    provider.setBankAccount(providerVo.getBankAccount());
    provider.setCellphone(providerVo.getCellphone());
    providerRepository.save(provider);
    return Result.succ();
  }

  private Specification<Provider> buildBaseQuery(ProviderSearchVo providerSearchVo) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (StringUtils.isNoneEmpty(providerSearchVo.getName())){
        predicates.add(criteriaBuilder.like(root.get("name"), "%" + providerSearchVo.getName() + "%"));
      }
      if (StringUtils.isNoneEmpty(providerSearchVo.getCellphone())){
        predicates.add(criteriaBuilder.like(root.get("cellphone"), "%" + providerSearchVo.getCellphone() + "%"));
      }
      return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
    };
  }
}
