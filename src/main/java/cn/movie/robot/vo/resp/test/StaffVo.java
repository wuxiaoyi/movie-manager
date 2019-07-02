package cn.movie.robot.vo.resp.test;

import lombok.Data;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.TypeName;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@TypeName("项目员工")
@Data
public class StaffVo {

  private int id;
  @Id
  private String name;
}
