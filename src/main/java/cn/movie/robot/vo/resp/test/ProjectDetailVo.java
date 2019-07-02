package cn.movie.robot.vo.resp.test;

import lombok.Data;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@TypeName("项目费用明细")
@Data
public class ProjectDetailVo {

  private int id;
  @Id
  private String name;
  @PropertyName("金额")
  private int money;
}
