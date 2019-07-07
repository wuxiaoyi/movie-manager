package cn.movie.robot.vo.resp.search;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/7
 */
@Data
public class ProjectSearchParentFeeRespVo {
  private Integer id;
  private Integer categoryId;
  private BigDecimal budgetAmount;
  private BigDecimal realAmount;
  private List<ProjectSearchChildFeeRespVo> childFeeList;
}
