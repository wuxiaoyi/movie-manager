package cn.movie.robot.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/7/4
 */
public enum ProjectStateEnum {

  PAUSE(1, "暂停"),
  CANCEL(2, "取消");

  ProjectStateEnum(int state, String name){
    this.state = state;
    this.name = name;
  }

  public static List<Integer> getStates(){
    return Arrays.stream(values()).map(ProjectStateEnum::getState).collect(Collectors.toList());
  }

  public static String getStateName(int state){
    return Arrays.stream(values())
        .filter(stateEnum -> stateEnum.getState() == state)
        .findFirst()
        .map(ProjectStateEnum::getName)
        .orElse(null);
  }

  private int state;
  private String name;

  public int getState() {
    return state;
  }

  public String getName() {
    return name;
  }
}
