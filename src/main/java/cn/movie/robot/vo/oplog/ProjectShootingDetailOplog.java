package cn.movie.robot.vo.oplog;

import lombok.Data;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Data
@TypeName("项目明细-拍摄费用")
public class ProjectShootingDetailOplog {

  @Id
  private Integer projectId;

  @PropertyName("费用明细")
  List<ProjectFeeDetailOplog> projectFeeDetailOplogList;
}
