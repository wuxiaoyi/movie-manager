package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.StaffRepository;
import cn.movie.robot.model.Staff;
import cn.movie.robot.service.IStaffService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.StaffSearchVo;
import cn.movie.robot.vo.req.StaffVo;
import cn.movie.robot.vo.resp.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Direction.DESC;

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
  public Result search(StaffSearchVo staffSearchVo) {
    Pageable pageable = PageRequest.of(staffSearchVo.getPage() - 1, staffSearchVo.getPageSize(), Sort.by(DESC, Constants.COMMON_FIELD_NAME_ID));
    Specification<Staff> specification = buildBaseQuery(staffSearchVo);

    Page<Staff> staffPage = staffRepository.findAll(specification, pageable);
    PageBean<Staff> staffPageBean = new PageBean<>(
        staffPage.getTotalElements(),
        staffPage.getTotalPages(),
        staffPage.getContent()
    );
    return Result.succ(staffPageBean);
  }

  @Override
  public Result queryAll() {
    return Result.succ(staffRepository.findAll());
  }

  @Override
  public Result updateState(Integer staffId, int state) {
    Staff staff = staffRepository.getOne(staffId);
    if (Objects.isNull(staff)){
      return Result.error("该员工不存在");
    }
    staff.setState(state);
    staffRepository.save(staff);
    return Result.succ();
  }

  @Override
  public Result save(StaffVo staffVo) {
    Staff staff = new Staff();
    staff.setName(staffVo.getName());
    staff.setAscription(staffVo.getAscription());
    staff.setCellphone(staffVo.getCellphone());
    staffRepository.save(staff);
    return Result.succ();
  }

  @Override
  public Result update(Integer staffId, StaffVo staffVo) {
    Staff staff = staffRepository.getOne(staffId);
    if (Objects.isNull(staff)){
      return Result.error("该员工不存在");
    }
    staff.setName(staffVo.getName());
    staff.setAscription(staffVo.getAscription());
    staff.setCellphone(staffVo.getCellphone());
    staffRepository.save(staff);
    return Result.succ();
  }

  private Specification<Staff> buildBaseQuery(StaffSearchVo staffSearchVo) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (StringUtils.isNoneEmpty(staffSearchVo.getName())){
        predicates.add(criteriaBuilder.like(root.get("name"), "%" + staffSearchVo.getName() + "%"));
      }
      if (StringUtils.isNoneEmpty(staffSearchVo.getCellphone())){
        predicates.add(criteriaBuilder.like(root.get("cellphone"), "%" + staffSearchVo.getCellphone() + "%"));
      }
      if (Objects.nonNull(staffSearchVo.getAscription())){
        predicates.add(criteriaBuilder.equal(root.get("ascription"), staffSearchVo.getAscription()));
      }
      return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
    };
  }
}
