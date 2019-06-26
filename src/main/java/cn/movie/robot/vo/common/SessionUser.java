package cn.movie.robot.vo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Wuxiaoyi
 * @date 2019/6/26
 */
@Data
public class SessionUser implements Serializable {
  private static final long serialVersionUID = 1L;

  private Integer id;
  private String name;
  private String email;
  private String cellphone;
}
