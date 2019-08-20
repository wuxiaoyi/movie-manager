package cn.movie.robot.vo.req.project;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@Data
public class ProjectBaseInfoVo {
  private String sid;

  private String name;

  private Integer contractSubjectId;

  private Integer companyId;

  private Integer childCompanyId;

  private Integer filmDuration;

  private Date shootingStartAt;

  private Integer shootingDuration;

  private BigDecimal contractAmount;

  private BigDecimal returnAmount;

  private List<ProjectMemberVo> projectMembers;
}
