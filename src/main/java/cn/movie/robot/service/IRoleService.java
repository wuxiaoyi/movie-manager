package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
public interface IRoleService {
  Result queryAll(Pageable pageable);

  Result normalAll();

  Result save(String roleName);

  Result updateUser(Integer userId, List<Integer> roleIds);

  Result updatePermission(Integer rolId, List<Integer> permissionIds);

  Result delete(Integer roleId);

  Result queryByUserId(Integer userId);
}
