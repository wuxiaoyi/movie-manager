package cn.movie.robot.controller;

import cn.movie.robot.common.Constants;
import cn.movie.robot.service.IContractSubjectService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.ContractSubjectVo;
import cn.movie.robot.vo.req.ProviderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@RestController
@RequestMapping(value = "/api/v1/contract_subjects", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ContractSubjectController {

  @Autowired
  private IContractSubjectService contractSubjectService;

  @GetMapping("")
  public Result list(@RequestParam("page") int page, @RequestParam("page_size") int pageSize){
    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));
    return contractSubjectService.queryAll(pageable);
  }

  @PostMapping("")
  public Result save(@RequestBody ContractSubjectVo contractSubjectVo){
    return contractSubjectService.save(contractSubjectVo.getName());
  }

  @DeleteMapping("/{contract_id}")
  public Result save(@PathVariable("contract_id") Integer contractId){
    return contractSubjectService.forbiddenContractSubject(contractId);
  }

  @PutMapping("/{contract_id}")
  public Result update(@PathVariable("contract_id") Integer contractId, @RequestBody ContractSubjectVo contractSubjectVo){
    return contractSubjectService.update(contractId, contractSubjectVo.getName());
  }
}
