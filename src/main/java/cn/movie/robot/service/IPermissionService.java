package cn.movie.robot.service;

import cn.movie.robot.model.Permission;
import cn.movie.robot.model.Role;
import cn.movie.robot.model.User;
import cn.movie.robot.vo.common.Result;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
public interface IPermissionService {
  List<Permission> queryPermissionByUser(User user);

  List<Role> queryRoleByUser(User user);

  Result queryAll(Pageable pageable);
}
