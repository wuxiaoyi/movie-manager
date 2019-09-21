package cn.movie.robot.vo.req;

import lombok.Data;

/**
 * @author Wuxiaoyi
 * @date 2019/9/21
 */
@Data
public class StaffSearchVo {
  private String name;
  private String cellphone;
  private Integer ascription;
  private Integer page = 1;
  private Integer pageSize = 20;
}
