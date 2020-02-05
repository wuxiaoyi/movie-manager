package cn.movie.robot.vo.resp;

import lombok.Data;

/**
 * @author Wuxiaoyi
 * @date 2020/2/4
 */
@Data
public class ProjectPermissionRespVo {
  private Integer id;
  private Integer userId;
  private Integer projectId;
  private String userName;
  private Integer permissionType;
}
