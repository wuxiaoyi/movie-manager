package cn.movie.robot.service;

import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.resp.MemberTypeVo;
import cn.movie.robot.vo.resp.ProjectStateVo;

import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
public interface IConfigService {
  Result memberTypes();
  Result projectStates();
}
