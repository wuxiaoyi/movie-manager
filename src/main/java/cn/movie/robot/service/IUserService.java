package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.SignUpVo;

/**
 * @author Wuxiaoyi
 * @date 2019/6/27
 */
public interface IUserService {
  /**
   * 生成注册key
   * @return
   */
  Result signUpKey();

  /**
   * 用户注册
   * @param signUpVo
   * @return
   */
  Result signUp(SignUpVo signUpVo);

  /**
   * 禁用用户
   * @param userId
   * @return
   */
  Result forbiddenUser(Integer userId);

  /**
   * 生成忘记密码key
   * @param email
   * @return
   */
  Result forgetPwdKey(String email);
}
