package cn.movie.robot.controller;

import cn.movie.robot.common.Constants;
import cn.movie.robot.service.IOplogService;
import cn.movie.robot.vo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@RestController
@RequestMapping(value = "/api/v1/operation_logs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OperationLogController {

  @Autowired
  IOplogService oplogService;

  @GetMapping("")
  public Result list(
      @RequestParam("page") int page, @RequestParam("page_size") int pageSize,
      @RequestParam("target_id") int targetId, @RequestParam("target_type") int targetType
  ){
    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));
    return oplogService.list(pageable, targetId, targetType);
  }
}
