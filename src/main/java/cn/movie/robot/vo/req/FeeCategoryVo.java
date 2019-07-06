package cn.movie.robot.vo.req;

import lombok.Data;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Data
public class FeeCategoryVo {
  private Integer id;

  private String name;

  private Integer categoryType;

  private Integer parentCategoryId;

  private int stage;
}
