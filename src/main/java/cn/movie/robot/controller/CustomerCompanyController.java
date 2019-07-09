package cn.movie.robot.controller;

import cn.movie.robot.common.Constants;
import cn.movie.robot.service.ICustomerCompanyService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ContractSubjectVo;
import cn.movie.robot.vo.req.CustomerCompanyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * @author Wuxiaoyi
 * @date 2019/7/9
 */
@RestController
@RequestMapping(value = "/api/v1/customer_company", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CustomerCompanyController {

  @Autowired
  ICustomerCompanyService customerCompanyService;

  @GetMapping("")
  public Result list(@RequestParam("page") int page, @RequestParam("page_size") int pageSize){
    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));
    return customerCompanyService.queryAll(pageable);
  }

  @PostMapping("")
  public Result save(@RequestBody CustomerCompanyVo customerCompanyVo){
    return customerCompanyService.save(customerCompanyVo);
  }

  @DeleteMapping("/{company_id}")
  public Result forbidden(@PathVariable("company_id") Integer companyId){
    return customerCompanyService.updateState(companyId, Constants.COMMON_STATE_FORBIDDEN);
  }

  @PutMapping("/{company_id}/recover")
  public Result recover(@PathVariable("company_id") Integer companyId){
    return customerCompanyService.updateState(companyId, Constants.COMMON_STATE_NORMAL);
  }

  @PutMapping("/{company_id}")
  public Result update(@PathVariable("company_id") Integer companyId, @RequestBody CustomerCompanyVo customerCompanyVo){
    return customerCompanyService.update(companyId, customerCompanyVo);
  }
}
