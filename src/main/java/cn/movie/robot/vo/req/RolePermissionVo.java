package cn.movie.robot.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/9/2
 */
@Data
public class RolePermissionVo {
  private String name;
  private List<Integer> permissionIds;
}
