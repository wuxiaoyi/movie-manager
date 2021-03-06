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
@Table(name = "project_detail")
@EntityListeners({AuditingEntityListener.class})
public class ProjectDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer projectId;

  private Integer stage;

  private Integer feeCategoryId;

  private Integer feeChildCategoryId;

  private String desc;

  private BigDecimal budgetAmount;

  private BigDecimal realAmount;

  private Integer providerId;

  private Integer rankScore;

  private String remark;

  @CreatedDate
  @Column(updatable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createdAt;

  @LastModifiedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updatedAt;
}
