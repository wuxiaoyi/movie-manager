package cn.movie.robot.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "fee_category")
@EntityListeners({AuditingEntityListener.class})
public class FeeCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull(message = "用户名不能为空")
  private String name;

  private Integer categoryType;

  private Integer parentCategoryId;

  private int state;

  @CreatedDate
  @Column(updatable = false)
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;
}
