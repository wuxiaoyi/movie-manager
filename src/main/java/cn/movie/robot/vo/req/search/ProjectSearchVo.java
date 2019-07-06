package cn.movie.robot.vo.req.search;

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
public class ProjectSearchVo {
  private int page;

  private int pageSize;

  private String sid;

  private String name;

  private List<Integer> states;

  private String companyName;

  private String companyGroup;

  private BigDecimal contractAmountStart;

  private BigDecimal contractAmountEnd;

  private BigDecimal realCostStart;

  private BigDecimal realCostEnd;

  private BigDecimal budgetCostStart;

  private BigDecimal budgetCostEnd;

  private Integer filmDurationStart;

  private Integer filmDurationEnd;

  private Date shootingStartAtStart;

  private Date shootingStartAtEnd;

  private Integer shootingDurationStart;

  private Integer shootingDurationEnd;

  private List<Integer> directorList;

  private List<Integer> producerList;

  private List<FeeSearchVo> feeList;
}
