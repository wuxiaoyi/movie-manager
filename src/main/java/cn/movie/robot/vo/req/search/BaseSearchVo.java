package cn.movie.robot.vo.req.search;

import lombok.Data;

/**
 * @author Wuxiaoyi
 * @date 2019/8/15
 */
@Data
public class BaseSearchVo {
  private String sid;
  private String projectName;
  private Integer state;
  private Integer contractId;
  private Integer page;
  private Integer pageSize;
}
