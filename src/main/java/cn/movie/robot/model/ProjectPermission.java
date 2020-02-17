package cn.movie.robot.model;

import cn.movie.robot.common.Constants;
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
 * @date 2020/2/4
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "project_permissions")
@EntityListeners({AuditingEntityListener.class})
public class ProjectPermission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer projectId;

  private Integer permissionType;

  private Integer operatorId;

  private Integer userId;

  @CreatedDate
  @Column(updatable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createdAt;

  @LastModifiedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updatedAt;

  public void buildByProject(Project project, Integer pType){
    this.permissionType = pType;
    this.userId = project.getCreatorId();
    this.operatorId = project.getCreatorId();
    this.projectId = project.getId();
  }
}
