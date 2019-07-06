package cn.movie.robot.vo.resp;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Data
public class ProjectFeeRespVo {
  private Integer id;

  private Integer stage;

  private Integer feeCategoryId;

  private Integer feeChildCategoryId;

  private BigDecimal budgetAmount;

  private BigDecimal realAmount;

  private Integer providerId;

  private Integer rankScore;

  private String remark;
}
