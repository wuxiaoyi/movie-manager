package cn.movie.robot.vo.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Wuxiaoyi
 * @date 2019/6/27
 */
@Data
public class SignUpVo {
  @NotNull(message = "名称不能为空")
  private String name;

  @NotNull(message = "邮箱不能为空")
  private String email;

  @NotNull(message = "电话不能为空")
  private String cellphone;

  @NotNull(message = "密码不能为空")
  private String password;
}
