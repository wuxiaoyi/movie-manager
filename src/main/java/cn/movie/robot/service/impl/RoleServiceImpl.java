package cn.movie.robot.service.impl;

import cn.movie.robot.dao.PermissionRoleRepository;
import cn.movie.robot.dao.RoleRepository;
import cn.movie.robot.model.Permission;
import cn.movie.robot.model.PermissionRole;
import cn.movie.robot.model.Role;
import cn.movie.robot.service.IRoleService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.resp.PageBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
@Service
public class RoleServiceImpl implements IRoleService {

  @Resource
  RoleRepository roleRepository;

  @Resource
  PermissionRoleRepository permissionRoleRepository;

  @Override
  public Result queryAll(Pageable pageable) {
    Page<Role> rolePage = roleRepository.findAll(pageable);
    PageBean<Role> rolePageBean = new PageBean<>(
        rolePage.getTotalElements(),
        rolePage.getTotalPages(),
        rolePage.getContent()
    );
    return Result.succ(rolePageBean);
  }

  @Override
  public Result save(String roleName) {
    Role existRole = roleRepository.findByName(roleName);
    if (Objects.nonNull(existRole)){
      return Result.error("该角色名称已存在");
    }
    Role role = new Role();
    role.setName(roleName);
    roleRepository.save(role);
    return Result.succ();
  }

  @Override
  public Result updatePermission(Integer rolId, String permissionIds) {
    Role role = roleRepository.getOne(rolId);
    if (Objects.isNull(role)){
      return Result.error("该角色不存在");
    }
    List<Integer> permissionIdList = Arrays.asList(permissionIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
    List<PermissionRole> permissionRoleList = permissionRoleRepository.findAllByRoleId(rolId);
    List<Integer> existPermissionIds = permissionRoleList.stream().map(PermissionRole::getPermissionId).collect(Collectors.toList());


    List<Integer> needAddIds = new ArrayList<>();
    List<Integer> needDeleteIds = new ArrayList<>();
    for (Integer newId : permissionIdList){
      if (!existPermissionIds.contains(newId)){
        needAddIds.add(newId);
      }
    }

    for (Integer oldId : existPermissionIds){
      if (!permissionIdList.contains(oldId)){
        needDeleteIds.add(oldId);
      }
    }

    if (needDeleteIds.size() > 0){
      permissionRoleRepository.deleteByRoleIdAndPermissionIdIn(rolId, needDeleteIds);
    }

    for (Integer permissionId : needAddIds){
      PermissionRole permissionRole = new PermissionRole();
      permissionRole.setRoleId(rolId);
      permissionRole.setPermissionId(permissionId);
      permissionRoleRepository.save(permissionRole);
    }

    return Result.succ();
  }
}
