package cn.movie.robot.vo.oplog;

import cn.movie.robot.model.Project;
import cn.movie.robot.utils.DateUtil;
import lombok.Data;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
@Data
@TypeName("项目基本信息")
public class ProjectBaseInfoOplog {

  public ProjectBaseInfoOplog(Project project){
    this.id = project.getId();
    this.sid = project.getSid();
    this.name = project.getName();
    this.filmDuration = project.getFilmDuration();
    if (Objects.nonNull(project.getShootingStartAt())){
      this.shootingStartAt = DateUtil.format_yyyy_MM_dd(project.getShootingStartAt());
    }
    this.shootingDuration = project.getShootingDuration();
    this.contractAmount = project.getContractAmount();
    this.returnAmount = project.getReturnAmount();
    this.realCost = project.getRealCost();
    this.budgetCost = project.getBudgetCost();
    this.shootingBudget = project.getShootingBudget();
    this.lateStateBudget = project.getLateStateBudget();
    this.lateStateCost = project.getLateStateCost();
  }

  @Id
  private Integer id;

  @PropertyName("项目ID")
  private String sid;

  @PropertyName("项目名称")
  private String name;

  @PropertyName("合同签署主体")
  private String contractSubjectName;

  @PropertyName("成片时长")
  private Integer filmDuration;

  @PropertyName("拍摄日期")
  private String shootingStartAt;

  @PropertyName("拍摄周期")
  private Integer shootingDuration;

  @PropertyName("项目合同金额")
  private BigDecimal contractAmount;

  @PropertyName("项目回款金额")
  private BigDecimal returnAmount;

  @PropertyName("项目实际总成本")
  private BigDecimal realCost;

  @PropertyName("项目预算总成本")
  private BigDecimal budgetCost;

  @PropertyName("项目拍摄预算")
  private BigDecimal shootingBudget;

  @PropertyName("项目后期预算")
  private BigDecimal lateStateBudget;

  @PropertyName("项目拍摄成本")
  private BigDecimal shootingCost;

  @PropertyName("项目后期成本")
  private BigDecimal lateStateCost;

  @PropertyName("状态")
  private String stateName;

  @PropertyName("人员配置")
  private List<ProjectMemberOplog> projectMemberOplogs;
}
