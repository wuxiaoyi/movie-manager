package cn.movie.robot.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Wuxiaoyi
 * @date 2019/6/24
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "roles")
@EntityListeners({AuditingEntityListener.class})
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  @CreatedDate
  @Column(updatable = false)
  private Date createdAt;
  @LastModifiedDate
  private Date updatedAt;
}
