package cn.movie.robot.vo.resp;

import lombok.Data;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Data
public class ProjectMemberRespVo {
  private int id;
  private int projectId;
  private int memberType;
  private int staffId;
}
