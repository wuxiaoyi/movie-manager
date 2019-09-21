package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.CustomerCompanyRepository;
import cn.movie.robot.model.ContractSubject;
import cn.movie.robot.model.CustomerCompany;
import cn.movie.robot.model.Staff;
import cn.movie.robot.service.ICustomerCompanyService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.CustomerCompanySearchVo;
import cn.movie.robot.vo.req.CustomerCompanyVo;
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
 * @date 2019/7/9
 */
@Service
public class CustomerCompanyServiceImpl implements ICustomerCompanyService {

  @Resource
  CustomerCompanyRepository customerCompanyRepository;

  @Override
  public Result queryAll(Pageable pageable) {
    Page<CustomerCompany> customerCompanyPage = customerCompanyRepository.findAll(pageable);
    PageBean<CustomerCompany> customerCompanyPageBean = new PageBean<>(
        customerCompanyPage.getTotalElements(),
        customerCompanyPage.getTotalPages(),
        customerCompanyPage.getContent()
    );
    return Result.succ(customerCompanyPageBean);
  }

  @Override
  public Result search(CustomerCompanySearchVo companySearchVo) {
    Pageable pageable = PageRequest.of(companySearchVo.getPage() - 1, companySearchVo.getPageSize(), Sort.by(DESC, Constants.COMMON_FIELD_NAME_ID));
    Specification<CustomerCompany> specification = buildBaseQuery(companySearchVo);

    Page<CustomerCompany> customerCompanyPage = customerCompanyRepository.findAll(specification, pageable);
    PageBean<CustomerCompany> companyPageBean = new PageBean<>(
        customerCompanyPage.getTotalElements(),
        customerCompanyPage.getTotalPages(),
        customerCompanyPage.getContent()
    );
    return Result.succ(companyPageBean);
  }

  @Override
  public Result queryAll() {
    return Result.succ(customerCompanyRepository.findAll());
  }

  @Override
  public Result updateState(Integer id, int state) {
    CustomerCompany customerCompany = customerCompanyRepository.getOne(id);
    if (Objects.isNull(customerCompany)){
      return Result.error("该客户公司不存在");
    }
    customerCompany.setState(state);
    customerCompanyRepository.save(customerCompany);
    return Result.succ();
  }

  @Override
  public Result save(CustomerCompanyVo customerCompanyVo) {
    CustomerCompany existCompany = customerCompanyRepository.findByName(customerCompanyVo.getName());
    if (Objects.nonNull(existCompany)){
      return Result.error("该客户公司名称已存在");
    }

    CustomerCompany company = new CustomerCompany();
    company.setName(customerCompanyVo.getName());
    company.setParentCompanyId(customerCompanyVo.getParentCompanyId());
    company.setCompanyType(customerCompanyVo.getCompanyType());
    customerCompanyRepository.save(company);
    return Result.succ();
  }

  @Override
  public Result update(Integer id, CustomerCompanyVo customerCompanyVo) {
    CustomerCompany customerCompany = customerCompanyRepository.getOne(id);
    if (Objects.isNull(customerCompany)){
      return Result.error("该客户公司不存在");
    }
    customerCompany.setName(customerCompanyVo.getName());
    customerCompany.setParentCompanyId(customerCompanyVo.getParentCompanyId());
    customerCompany.setCompanyType(customerCompanyVo.getCompanyType());
    customerCompanyRepository.save(customerCompany);
    return Result.succ();
  }

  private Specification<CustomerCompany> buildBaseQuery(CustomerCompanySearchVo companySearchVo) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (StringUtils.isNoneEmpty(companySearchVo.getName())){
        predicates.add(criteriaBuilder.like(root.get("name"), "%" + companySearchVo.getName() + "%"));
      }
      if (Objects.nonNull(companySearchVo.getCompanyType())){
        predicates.add(criteriaBuilder.equal(root.get("companyType"), companySearchVo.getCompanyType()));
      }
      return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
    };
  }
}
