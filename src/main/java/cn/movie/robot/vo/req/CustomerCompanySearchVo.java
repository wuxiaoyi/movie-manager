package cn.movie.robot.vo.req;

import lombok.Data;

/**
 * @author Wuxiaoyi
 * @date 2019/7/9
 */
@Data
public class CustomerCompanySearchVo {
  private String name;
  private Integer companyType;
  private Integer page = 1;
  private Integer pageSize = 20;
}
