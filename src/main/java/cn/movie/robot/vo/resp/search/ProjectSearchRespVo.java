package cn.movie.robot.vo.resp.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/7
 */
@Data
public class ProjectSearchRespVo {
  private int id;
  private String sid;
  private String name;
  private Integer contractSubjectId;
  private Integer state;
  private BigDecimal contractAmount;
  private BigDecimal realCost;
  private BigDecimal budgetCost;
  private BigDecimal shootingBudget;
  private BigDecimal lateStateBudget;
  private BigDecimal shootingCost;
  private BigDecimal lateStateCost;
  private String filmDuration;

  private String projectLeaderList;

  private String customerManagerList;

  private String executiveDirecrotList;

  private String copyWritingList;

  private String postEditingList;

  private String compositingList;

  private String artList;

  private String musicList;

  private String storyBoardList;

  private String directorList;

  private String producerList;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
  private Date shootingStartAt;
  private String shootingDuration;
  private List<ProjectSearchParentFeeRespVo> projectDetailList;
}
