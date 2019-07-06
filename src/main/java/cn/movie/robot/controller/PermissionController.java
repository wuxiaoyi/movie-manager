package cn.movie.robot.controller;

import cn.movie.robot.common.Constants;
import cn.movie.robot.service.IPermissionService;
import cn.movie.robot.vo.common.Result;
import lombok.NonNull;
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
 * @date 2019/6/26
 */
@RestController
@RequestMapping(value = "/api/v1/permissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PermissionController {

  @Autowired
  IPermissionService permissionService;

  @GetMapping("")
  public Result list(@RequestParam("page") int page, @RequestParam("page_size") int pageSize){
    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));
    return permissionService.queryAll(pageable);
  }

  @GetMapping("/normal_all")
  public Result all(){
    return permissionService.queryAll();
  }
}
