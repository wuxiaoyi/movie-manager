package cn.movie.robot.controller;

import cn.movie.robot.common.Constants;
import cn.movie.robot.service.IStaffService;
import cn.movie.robot.vo.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * @author Wuxiaoyi
 * @date 2019/7/1
 */
@RestController
@RequestMapping(value = "/api/v1/staffs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StaffController {

  @Autowired
  IStaffService staffService;

  @GetMapping("")
  public Result list(@RequestParam("page") int page, @RequestParam("page_size") int pageSize){
    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by(ASC, Constants.COMMON_FIELD_NAME_ID));
    return staffService.queryAll(pageable);
  }

  @PostMapping("")
  public Result save(@RequestParam("name") String name, @RequestParam("ascription") Integer ascription){
    return staffService.save(name, ascription);
  }

  @DeleteMapping("/{staff_id}")
  public Result save(@PathVariable("staff_id") Integer staffId){
    return staffService.forbiddenStaff(staffId);
  }

}
