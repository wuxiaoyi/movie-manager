package cn.movie.robot.common;

import java.math.BigDecimal;

/**
 * @author Wuxiaoyi
 * @date 2019/6/12
 */
public class Constants {
  public static final String COMMON_FIELD_NAME_ID = "id";

  /**
   * 错误码相关 start
   */
  public static final int SUCCESS_CODE = 0;
  public static final int ERROR_CODE = 1;
  public static final String ERROR_MSG = "error";

  public static final int NO_AUTH_ERROR_CODE = 101;
  public static final String NO_AUTH_ERROR_MSG = "请登录";

  public static final int NO_PERMISSION_ERROR_CODE = 102;
  public static final String NO_PERMISSION_ERROR_MSG = "无此功能权限";

  public static final int UNKNOW_EXCEPTION_ERROR_CODE = 103;
  public static final String UNKNOW_EXCEPTION_ERROR_MSG = "系统异常";

  /**
   * 错误码相关 end
   */

  public static final int COMMON_STATE_NORMAL = 0;
  public static final int COMMON_STATE_FORBIDDEN = 1;

  /**
   * 登录注册相关 start
   */

  public static final String USER_SIGN_UP_KEY_PREFIX = "user_sign_up:";
  public static final String USER_FORGET_PWD_KEY_PREFIX = "user_forget_pwd:";

  /**
   * 登录注册相关 end
   */

  /**
   * 员工相关 start
   */

  /**
   * 员工归属，0内部，1外部
   */
  public static final int STAFF_ASCRIPTION_INTERNAL = 1;
  public static final int STAFF_ASCRIPTION_EXTERNAL = 2;

  /**
   * 员工相关 end
   */

}
