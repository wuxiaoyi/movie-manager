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

  private Integer companyId;

  private Integer childCompanyId;

  //合同金额
  private BigDecimal contractAmountStart;

  private BigDecimal contractAmountEnd;

  //回款金额
  private BigDecimal returnAmountStart;

  private BigDecimal returnAmountEnd;

  //实际总金额
  private BigDecimal realCostStart;

  private BigDecimal realCostEnd;

  //预算总金额
  private BigDecimal budgetCostStart;

  private BigDecimal budgetCostEnd;

  //拍摄预算金额
  private BigDecimal shootingBudgetCostStart;

  private BigDecimal shootingBudgetCostEnd;

  //后期预算金额
  private BigDecimal lateStateBudgetCostStart;

  private BigDecimal lateStateBudgetCostEnd;

  //拍摄实际金额
  private BigDecimal shootingRealCostStart;

  private BigDecimal shootingRealCostEnd;

  //后期实际金额
  private BigDecimal lateStateRealCostStart;

  private BigDecimal lateStateRealCostEnd;

  private Integer filmDurationStart;

  private Integer filmDurationEnd;

  private Date shootingStartAtStart;

  private Date shootingStartAtEnd;

  private Integer shootingDurationStart;

  private Integer shootingDurationEnd;

  private List<Integer> projectLeaderList;

  private List<Integer> customerManagerList;

  private List<Integer> executiveDirecrotList;

  private List<Integer> copyWritingList;

  private List<Integer> postEditingList;

  private List<Integer> compositingList;

  private List<Integer> artList;

  private List<Integer> musicList;

  private List<Integer> storyBoardList;

  private List<Integer> directorList;

  private List<Integer> producerList;

  private List<FeeSearchVo> feeList;

  private List<Integer> providerList;
}
