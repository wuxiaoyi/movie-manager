package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.SignUpVo;
import org.springframework.data.domain.Pageable;

/**
 * @author Wuxiaoyi
 * @date 2019/6/27
 */
public interface IUserService {
  /**
   * 生成忘记密码key
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
   * 更新状态
   * @param userId
   * @return
   */
  Result updateState(Integer userId, int state);

  /**
   * 生成忘记密码key
   * @param email
   * @return
   */
//  Result forgetPwdKey(String email);

  /**
   * 重置密码
   * @param userId
   * @param password
   * @return
   */
  Result resetPwd(Integer userId, String password);

  /**
   * 查询所有后台用户
   * @param pageable
   * @return
   */
  Result queryAll(Pageable pageable);
}
