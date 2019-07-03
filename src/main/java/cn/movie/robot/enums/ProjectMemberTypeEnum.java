package cn.movie.robot.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/7/3
 */
public enum ProjectMemberTypeEnum {

  PROJECT_LEADER(1, "项目负责人"),
  CUSTOMER_MANAGER(2, "客户对接人"),
  DIRECTOR(3, "导演"),
  EXECUTIVE_DIRECTOR(4, "执行导演"),
  COPYWRITING(5, "文案"),
  PRODUCER(6, "制片"),
  POST_EDITING(7, "后期剪辑"),
  COMPOSITING(8, "后期合成"),
  ART(9, "美术"),
  MUSIC(10, "音乐"),
  STORY_BOARD(11, "分镜");

  ProjectMemberTypeEnum(int type, String name){
    this.type = type;
    this.name = name;
  }

  public static List<Integer> getMemberTypes(){
    return Arrays.stream(values()).map(ProjectMemberTypeEnum::getType).collect(Collectors.toList());
  }

  public static String getTypeName(int type){
    return Arrays.stream(values())
        .filter(typeEnum -> typeEnum.getType() == type)
        .findFirst()
        .map(ProjectMemberTypeEnum::getName)
        .orElse(null);
  }

  private int type;
  private String name;

  public int getType() {
    return type;
  }

  public String getName() {
    return name;
  }
}
