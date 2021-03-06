package cn.movie.robot.controller;

import cn.movie.robot.service.*;
import cn.movie.robot.vo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@RestController
@RequestMapping(value = "/api/v1/configs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ConfigController {

  @Autowired
  private IContractSubjectService contractSubjectService;

  @Autowired
  private IProviderService providerService;

  @Autowired
  private IStaffService staffService;

  @Autowired
  private IFeeCategoryService feeCategoryService;

  @Autowired
  private IConfigService configService;

  @Autowired
  private ICustomerCompanyService customerCompanyService;

  @Autowired
  private IUserService userService;

  @GetMapping("/contract_subjects")
  public Result contractSubjects(){
    return contractSubjectService.queryAll();
  }

  @GetMapping("/providers")
  public Result providers(){
    return providerService.queryAll();
  }

  @GetMapping("/staffs")
  public Result staffs(){
    return staffService.queryAll();
  }

  @GetMapping("/fee_categories")
  public Result feeCategories(){
    return feeCategoryService.queryAll();
  }

  @GetMapping("/member_types")
  public Result memberTypes(){
    return configService.memberTypes();
  }

  @GetMapping("/project_states")
  public Result projectStates(){
    return configService.projectStates();
  }

  @GetMapping("/customer_companies")
  public Result customerCompanies(){
    return customerCompanyService.queryAll();
  }

  @GetMapping("/users")
  public Result users(){
    return userService.queryAll();
  }
}
