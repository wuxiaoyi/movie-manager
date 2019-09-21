package cn.movie.robot.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Data
public class ProjectDetailRespVo {
  private int projectId;
  private String projectName;
  private String projectSid;
  private List<ProjectFeeRespVo> projectFees;
}
