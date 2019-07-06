package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.StaffRepository;
import cn.movie.robot.model.Staff;
import cn.movie.robot.service.IStaffService;
import cn.movie.robot.utils.DateUtil;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.resp.PageBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author Wuxiaoyi
 * @date 2019/7/1
 */
@Service
public class StaffServiceImpl implements IStaffService {

  @Resource
  StaffRepository staffRepository;

  @Override
  public Result queryAll(Pageable pageable) {
    Page<Staff> staffPage = staffRepository.findAll(pageable);
    PageBean<Staff> staffPageBean = new PageBean<>(
        staffPage.getTotalElements(),
        staffPage.getTotalPages(),
        staffPage.getContent()
    );
    return Result.succ(staffPageBean);
  }

  @Override
  public Result queryNormal(Integer ascription) {
    List<Staff> staffList;
    if (ascription == Constants.STAFF_ASCRIPTION_INTERNAL){
      staffList = staffRepository.queryByStateAndAscription(Constants.COMMON_STATE_NORMAL, ascription);
    }else if (ascription == Constants.STAFF_ASCRIPTION_EXTERNAL){
      staffList = staffRepository.queryByStateAndAscription(Constants.COMMON_STATE_NORMAL, ascription);
    }else {
      staffList = staffRepository.queryByState(Constants.COMMON_STATE_NORMAL);
    }
    return Result.succ(staffList);
  }

  @Override
  public Result forbiddenStaff(Integer staffId) {
    Staff staff = staffRepository.getOne(staffId);
    if (Objects.isNull(staff)){
      return Result.error("该员工不存在");
    }
    staff.setState(Constants.COMMON_STATE_FORBIDDEN);
    staffRepository.save(staff);
    return Result.succ();
  }

  @Override
  public Result save(String name, Integer ascription) {
    Staff staff = new Staff();
    staff.setName(name);
    staff.setAscription(ascription);
    staffRepository.save(staff);
    return Result.succ();
  }
}