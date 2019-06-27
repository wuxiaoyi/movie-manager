package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import org.springframework.data.domain.Pageable;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
public interface IRoleService {
  Result queryAll(Pageable pageable);

  Result save(String roleName);

  Result updatePermission(Integer rolId, String permissionIds);
}
