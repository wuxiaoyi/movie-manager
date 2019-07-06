package cn.movie.robot.service.impl;

import cn.movie.robot.dao.PermissionRepository;
import cn.movie.robot.dao.PermissionRoleRepository;
import cn.movie.robot.dao.RoleRepository;
import cn.movie.robot.dao.UserRoleRepository;
import cn.movie.robot.model.*;
import cn.movie.robot.service.IPermissionService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.resp.PageBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
@Service
public class PermissionServiceImpl implements IPermissionService {

  @Resource
  UserRoleRepository userRoleRepository;

  @Resource
  PermissionRoleRepository permissionRoleRepository;

  @Resource
  PermissionRepository permissionRepository;

  @Resource
  RoleRepository roleRepository;

  @Override
  public List<Permission> queryPermissionByUser(User user) {
    List<UserRole> userRoleList = userRoleRepository.findAllByUserId(user.getId());
    List<Integer> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
    List<PermissionRole> permissionRoleList = permissionRoleRepository.findAllByRoleIdIn(roleIds);
    List<Integer> permissionIds = permissionRoleList.stream().map(PermissionRole::getPermissionId).collect(Collectors.toList());
    List<Permission> permissions = permissionRepository.findAllByIdIn(permissionIds);
    return permissions;
  }

  @Override
  public List<Role> queryRoleByUser(User user) {
    List<UserRole> userRoleList = userRoleRepository.findAllByUserId(user.getId());
    List<Integer> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
    List<Role> roles = roleRepository.findAllByIdIn(roleIds);
    return roles;
  }

  @Override
  public Result queryAll(Pageable pageable) {
    Page<Permission> permissionPageable = permissionRepository.findAll(pageable);
    PageBean<Permission> permissionPageBean = new PageBean<>(
        permissionPageable.getTotalElements(),
        permissionPageable.getTotalPages(),
        permissionPageable.getContent()
    );
    return Result.succ(permissionPageBean);
  }

  @Override
  public Result queryAll() {
    return Result.succ(permissionRepository.findAll());
  }
}
