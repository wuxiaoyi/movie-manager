package cn.movie.robot.common;

import java.math.BigDecimal;

/**
 * @author Wuxiaoyi
 * @date 2019/6/12
 */
public class Constants {
  public static final int SUCCESS_CODE = 0;
  public static final int ERROR_CODE = 1;
  public static final String ERROR_MSG = "error";

  public static final BigDecimal buyRatio = new BigDecimal(0.98);
  public static final BigDecimal sellRatio = new BigDecimal(1.05);
}
