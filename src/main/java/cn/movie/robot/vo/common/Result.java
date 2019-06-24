package cn.movie.robot.vo.common;

import cn.movie.robot.common.Constants;
import lombok.Data;


/**
 * @author 刘宇泽
 * @date 2018/7/5
 */
@Data
public class Result {

  public Integer code;

  public String msg;

  public Object data;

  public static Result fail() {
    return new Result(Constants.ERROR_CODE);
  }

  public static Result fail(String msg) {
    return new Result(Constants.ERROR_CODE, msg);
  }

  public static Result succ() {
    return new Result(Constants.SUCCESS_CODE);
  }

  public static Result succ(Object data) {
    return new Result(Constants.SUCCESS_CODE, null, data);
  }

  public static Result error() {
    return error(Constants.ERROR_MSG);
  }

  public static Result error(String msg) {
    Result result = new Result();
    result.setMsg(msg);
    result.setCode(Constants.ERROR_CODE);
    return result;
  }

  public Result() {
  }

  public Result(Integer code) {
    this.code = code;
  }

  public Result(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Result(Integer code, String msg, Object data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }
}
