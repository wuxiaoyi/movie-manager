package cn.movie.robot.controller;

import cn.movie.robot.service.IContractSubjectService;
import cn.movie.robot.service.IFeeCategoryService;
import cn.movie.robot.service.IProviderService;
import cn.movie.robot.service.IStaffService;
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

  @GetMapping("/contract_subjects")
  public Result contractSubjects(){
    return contractSubjectService.queryNormal();
  }

  @GetMapping("/providers")
  public Result providers(){
    return providerService.queryNormal();
  }

  @GetMapping("/staffs")
  public Result staffs(@RequestParam("ascription") Integer ascription){
    return staffService.queryNormal(ascription);
  }

  @GetMapping("/fee_categories")
  public Result fee_categories(){
    return feeCategoryService.queryNormal();
  }
}
