package cn.movie.robot.vo.oplog;

import lombok.Data;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
@Data
@TypeName("项目成员")
public class ProjectMemberOplog {
  @Id
  private String memberType;

  @PropertyName("人员配置")
  private List<String> memberList;
}
