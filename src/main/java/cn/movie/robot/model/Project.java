package cn.movie.robot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Wuxiaoyi
 * @date 2019/7/1
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "projects")
@EntityListeners({AuditingEntityListener.class})
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String sid;

  private String name;

  /**
   * 合同签署主体
   */
  private Integer contractSubjectId;

  /**
   * 客户公司（一级）
   */
  private Integer companyId;
  
  /**
   * 客户公司（二级）
   */
  private Integer childCompanyId;

  /**
   * 成片时长
   */
  private Integer filmDuration;

  /**
   * 拍摄日期
   */
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date shootingStartAt;

  /**
   * 拍摄周期
   */
  private Integer shootingDuration;

  /**
   * 项目合同金额
   */
  private BigDecimal contractAmount;

  /**
   * 项目回款金额
   */
  private BigDecimal returnAmount;

  /**
   * 项目实际总成本
   */
  private BigDecimal realCost;

  /**
   * 项目预算总成本
   */
  private BigDecimal budgetCost;

  /**
   * 项目拍摄预算
   */
  private BigDecimal shootingBudget;

  /**
   * 项目后期预算
   */
  private BigDecimal lateStateBudget;

  /**
   * 项目拍摄成本
   */
  private BigDecimal shootingCost;

  /**
   * 项目后期成本
   */
  private BigDecimal lateStateCost;

  private Integer state;

  @CreatedDate
  @Column(updatable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createdAt;

  @LastModifiedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updatedAt;
}
