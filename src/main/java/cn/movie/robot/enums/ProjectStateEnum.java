package cn.movie.robot.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/7/4
 */
public enum ProjectStateEnum {

  PLAN_STATE(10, "策划阶段"),
  DIFF_STATE(20, "比稿阶段"),
  SHOOTING_STATE(30, "拍摄阶段"),
  POST_EDIT_STATE(40, "后期阶段"),
  SUBMIT_STATE(50, "已交片"),
  INVOICE_STATE(60, "已开票"),
  PART_PAY_STATE(70, "部分回款"),
  ALL_PAY_STATE(80, "全部回款"),
  FINISH_STATE(90, "完成"),
  PAUSE_STATE(100, "暂停"),
  CANCEL_STATE(110, "取消");


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

  public static boolean isCancel(int state){
    return state == CANCEL_STATE.getState();
  }

  public static boolean isPause(int state){
    return state == PAUSE_STATE.getState();
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
