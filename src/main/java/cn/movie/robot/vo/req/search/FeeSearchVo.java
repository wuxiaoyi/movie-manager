package cn.movie.robot.vo.req.search;

import lombok.Data;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Data
public class FeeSearchVo {
  /**
   * 费用类别
   */
  private int categoryType;
  /**
   * 一级费用id
   */
  private int categoryId;
  /**
   * 二级费用ids
   */
  private List<Integer> childFeeCategoryList;

}
