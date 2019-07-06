package cn.movie.robot.vo.oplog;

import lombok.Data;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import java.math.BigDecimal;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Data
@TypeName("费用项")
public class ProjectFeeDetailOplog {
  @Id
  private String feeChildCategoryName;
  @PropertyName("费用一级类别")
  private String feeCategoryName;
  @PropertyName("供应商")
  private String providerName;
  @PropertyName("预算金额")
  private BigDecimal budgetAmount;
  @PropertyName("实际金额")
  private BigDecimal realAmount;
  @PropertyName("评分")
  private Integer rankScore;
  @PropertyName("备注")
  private String remark;

  @Override
  public String toString() {
    return
        ": 一级费用:" + feeCategoryName +
        ", 二级费用:" + feeChildCategoryName +
        ", 供应商:'" + providerName +
        ", 预算金额:" + budgetAmount +
        ", 实际金额:" + realAmount +
        ", 评分:" + rankScore +
        ", 配注:" + remark;
  }
}
