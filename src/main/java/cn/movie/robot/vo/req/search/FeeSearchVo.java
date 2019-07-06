package cn.movie.robot.vo.req.search;

import lombok.Data;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Data
public class FeeSearchVo {
  private int categoryType;
  private List<Integer> feeCategoryList;
}
