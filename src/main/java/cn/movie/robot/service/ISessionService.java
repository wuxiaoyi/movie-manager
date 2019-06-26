package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
public interface ISessionService {
  Result login(String email, String password);
}
