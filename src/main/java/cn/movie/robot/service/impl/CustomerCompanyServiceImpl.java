package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.CustomerCompanyRepository;
import cn.movie.robot.model.ContractSubject;
import cn.movie.robot.model.CustomerCompany;
import cn.movie.robot.service.ICustomerCompanyService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.CustomerCompanyVo;
import cn.movie.robot.vo.resp.PageBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

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
  public Result queryAll() {
    return Result.succ(customerCompanyRepository.findAll());
  }

  @Override
  public Result updateState(Integer id, int state) {
    CustomerCompany customerCompany = customerCompanyRepository.getOne(id);
    if (Objects.isNull(customerCompany)){
      return Result.error("该客户公司不存在");
    }
    customerCompany.setState(Constants.COMMON_STATE_FORBIDDEN);
    customerCompanyRepository.save(customerCompany);
    return Result.succ();
  }

  @Override
  public Result save(CustomerCompanyVo customerCompanyVo) {
    CustomerCompany company = new CustomerCompany();
    company.setName(customerCompanyVo.getName());
    company.setParentCompanyId(customerCompanyVo.getParentCompanyId());
    company.setCompanyType(customerCompanyVo.getCompanyType());
    customerCompanyRepository.save(company);
    return null;
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
}
