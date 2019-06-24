package cn.movie.robot.common;

/**
 * @author 刘宇泽
 * @date 2018/7/10
 */
public interface EntityVisibility {

  interface Result {
  }

  /**
   * 分页
   */
  interface Page extends Result {
  }

  /**
   * 摘要
   */
  interface Abstract extends Page {
  }

}
