package cn.movie.robot.controller;

import cn.movie.robot.common.Constants;
import cn.movie.robot.service.IProviderService;
import cn.movie.robot.vo.common.Result;
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
@RequestMapping(value = "/api/v1/providers", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProviderController {

  @Autowired
  IProviderService providerService;

  @GetMapping("")
  public Result list(@RequestParam("page") int page, @RequestParam("page_size") int pageSize){
    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));
    return providerService.queryAll(pageable);
  }

  @GetMapping("/normal_all")
  public Result queryAllNormal(){
    return providerService.queryNormal();
  }

  @PostMapping("")
  public Result save(@RequestBody ProviderVo providerVo){
    return providerService.save(providerVo);
  }

  @DeleteMapping("/{provider_id}")
  public Result save(@PathVariable("provider_id") Integer providerId){
    return providerService.forbiddenProvider(providerId);
  }

}
