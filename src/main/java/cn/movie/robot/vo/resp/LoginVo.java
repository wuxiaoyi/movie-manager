package cn.movie.robot.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
@Data
public class LoginVo {
  private String authCode;
  private List<String> permissions;
}
