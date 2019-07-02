package cn.movie.robot.vo.resp.test;

import lombok.Data;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.TypeName;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@TypeName("项目基本信息")
@Data
public class ProjectVo {
  @Id
  private int id;
  private List<StaffVo> staffVos;
  private List<ProjectDetailVo> projectDetailVos;
}
