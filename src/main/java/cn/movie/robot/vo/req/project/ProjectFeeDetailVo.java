package cn.movie.robot.vo.req.project;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Data
public class ProjectFeeDetailVo {
  private Integer id;

  private int projectId;

  private int feeCategoryId;

  private int feeChildCategoryId;

  private BigDecimal budgetAmount;

  private BigDecimal realAmount;

  private Integer providerId;

  private Integer rankScore;

  private String remark;
}
