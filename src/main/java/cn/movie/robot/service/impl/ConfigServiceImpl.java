package cn.movie.robot.service.impl;

import cn.movie.robot.enums.ProjectMemberTypeEnum;
import cn.movie.robot.enums.ProjectStateEnum;
import cn.movie.robot.service.IConfigService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.resp.MemberTypeVo;
import cn.movie.robot.vo.resp.ProjectStateVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/7/6
 */
@Service
public class ConfigServiceImpl implements IConfigService {
  @Override
  public Result memberTypes() {
    List<MemberTypeVo> memberTypeVos = new ArrayList<>();
    for (ProjectMemberTypeEnum typeEnum : ProjectMemberTypeEnum.values()){
      MemberTypeVo memberTypeVo = new MemberTypeVo();
      memberTypeVo.setType(typeEnum.getType());
      memberTypeVo.setName(typeEnum.getName());
      memberTypeVos.add(memberTypeVo);
    }
    return Result.succ(memberTypeVos);
  }

  @Override
  public Result projectStates() {
    List<ProjectStateVo> projectStateVos = new ArrayList<>();
    for (ProjectStateEnum stateEnum : ProjectStateEnum.values()){
      ProjectStateVo projectStateVo = new ProjectStateVo();
      projectStateVo.setName(stateEnum.getName());
      projectStateVo.setState(stateEnum.getState());
      projectStateVos.add(projectStateVo);
    }
    return Result.succ(projectStateVos);
  }
}
