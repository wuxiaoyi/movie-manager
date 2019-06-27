package cn.movie.robot.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
@Data
public class PageBean<T> {
  private long total;
  private int pages;
  private List<T> data;

  public PageBean() {

  }

  public PageBean(long total, int pages, List<T> data) {
    this.total = total;
    this.pages = pages;
    this.data = data;
  }
}
