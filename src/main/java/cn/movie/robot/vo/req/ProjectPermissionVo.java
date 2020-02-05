package cn.movie.robot.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2020/2/4
 */
@Data
public class ProjectPermissionVo {
  private Integer permissionType;
  private List<Integer> userIds;
}
