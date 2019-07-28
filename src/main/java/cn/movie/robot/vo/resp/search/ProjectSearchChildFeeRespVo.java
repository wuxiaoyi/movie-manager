package cn.movie.robot.vo.resp.search;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Wuxiaoyi
 * @date 2019/7/7
 */
@Data
public class ProjectSearchChildFeeRespVo {
  private Integer id;
  private Integer categoryId;
  private BigDecimal budgetAmount;
  private BigDecimal realAmount;
  private Integer providerId;
  private String providerName;
}
