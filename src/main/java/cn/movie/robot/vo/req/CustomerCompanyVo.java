package cn.movie.robot.vo.req;

import lombok.Data;

/**
 * @author Wuxiaoyi
 * @date 2019/7/9
 */
@Data
public class CustomerCompanyVo {
  private Integer id;
  private String name;
  private Integer companyType;
  private Integer parentCompanyId;
  private int state;
}
