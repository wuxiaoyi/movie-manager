package cn.movie.robot.vo.req;

import lombok.Data;

/**
 * @author Wuxiaoyi
 * @date 2019/7/2
 */
@Data
public class ProviderSearchVo {
  private String name;
  private String cellphone;
  private Integer page = 1;
  private Integer pageSize = 20;
}
