package cn.movie.robot.common;

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
