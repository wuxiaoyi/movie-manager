package cn.movie.robot.vo.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Wuxiaoyi
 * @date 2019/9/11
 */
@Data
public class UserVo {
  @NotNull(message = "名称不能为空")
  private String name;

  @NotNull(message = "邮箱不能为空")
  private String email;

  @NotNull(message = "电话不能为空")
  private String cellphone;
}
