package cn.movie.robot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @date 2019/7/3
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "operation_logs")
@EntityListeners({AuditingEntityListener.class})
public class OperationLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private int targetId;

  private int targetType;

  private String operationInfo;

  private int operatorId;

  private String operatorName;

  @CreatedDate
  @Column(updatable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createdAt;

  @LastModifiedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updatedAt;
}
