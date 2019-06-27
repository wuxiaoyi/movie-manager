package cn.movie.robot.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Wuxiaoyi
 * @date 2019/6/24
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "users")
@EntityListeners({AuditingEntityListener.class})
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotNull(message = "用户名不能为空")
  private String name;
  @NotNull(message = "邮箱不能为空")
  private String email;
  @NotNull(message = "电话不能为空")
  private String cellphone;
  @NotNull(message = "密码不能为空")
  private String password;
  @NotNull(message = "密码盐不能为空")
  private String passwordSlat;
  @CreatedDate
  @Column(updatable = false)
  private Date createdAt;
  @LastModifiedDate
  private Date updatedAt;
}
