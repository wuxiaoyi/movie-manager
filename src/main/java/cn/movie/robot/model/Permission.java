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
 * @date 2019/6/24
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "permissions")
@EntityListeners({AuditingEntityListener.class})
public class Permission {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull(message = "权限名称不能为空")
  private String name;
  @NotNull(message = "权限描述不能为空")
  private String desc;
  @CreatedDate
  @Column(updatable = false)
  private Date createdAt;
  @LastModifiedDate
  private Date updatedAt;
}
