package cn.movie.robot.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Data
public class ProjectRespVo {

  private Integer id;

  private String sid;

  private String name;

  private Integer contractSubjectId;

  private Integer filmDuration;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date shootingStartAt;

  private Integer shootingDuration;

  private BigDecimal contractAmount;

  private BigDecimal returnAmount;

  private BigDecimal realCost;

  private BigDecimal budgetCost;

  private BigDecimal shootingBudget;

  private BigDecimal lateStateBudget;

  private BigDecimal shootingCost;

  private BigDecimal lateStateCost;

  private Integer state;

  private List<ProjectMemberRespVo> projectMembers;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updatedAt;
}
