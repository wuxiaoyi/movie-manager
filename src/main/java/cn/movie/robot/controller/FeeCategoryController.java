package cn.movie.robot.controller;

import cn.movie.robot.common.Constants;
import cn.movie.robot.service.IFeeCategoryService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.FeeCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@RestController
@RequestMapping(value = "/api/v1/fee_categories", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FeeCategoryController {

  @Autowired
  IFeeCategoryService feeCategoryService;

  @GetMapping("")
  public Result list(@RequestParam("page") int page, @RequestParam("page_size") int pageSize){
    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));
    return feeCategoryService.queryAll(pageable);
  }

  @PostMapping("")
  public Result save(@RequestBody FeeCategoryVo feeCategoryVo){
    return feeCategoryService.save(feeCategoryVo);
  }

  @DeleteMapping("/{fee_category_id}")
  public Result forbidden(@PathVariable("fee_category_id") Integer id){
    return feeCategoryService.updateState(id, Constants.COMMON_STATE_FORBIDDEN);
  }

  @PutMapping("/{fee_category_id}/recover")
  public Result recover(@PathVariable("fee_category_id") Integer id){
    return feeCategoryService.updateState(id, Constants.COMMON_STATE_NORMAL);
  }

  @PutMapping("/{fee_category_id}")
  public Result update(@PathVariable("fee_category_id") Integer id, @RequestBody FeeCategoryVo feeCategoryVo){
    return feeCategoryService.update(id, feeCategoryVo);
  }
}
