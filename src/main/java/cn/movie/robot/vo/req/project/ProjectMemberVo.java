package cn.movie.robot.vo.req.project;

import lombok.Data;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
@Data
public class ProjectMemberVo {
  private Integer id;

  private Integer projectId;

  private Integer memberType;

  private Integer staffId;
}
