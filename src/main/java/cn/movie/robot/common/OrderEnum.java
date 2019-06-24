package cn.movie.robot.common;

/**
 * @author Wuxiaoyi
 * @date 2019/6/20
 */
public enum OrderEnum {

  //stateName 与火币保持一致
  //提交中
  SUBMITTING(0, "submitting"),
  //已提交
  SUBMITTED(1, "submitted"),
  //部分完成
  PARTIAL_FINISHED(2, "partial-filled"),
  //部分取消
  PARTIAL_CANCELED(3, "partial-canceled"),
  //已完成
  FINISHED(4, "filled"),
  //已取消
  CANCELED(5, "canceled");

  OrderEnum(int orderState, String stateName){
    this.orderState = orderState;
    this.stateName = stateName;
  }

  public static OrderEnum findByStateName(String stateName){
    for (OrderEnum orderEnum : OrderEnum.values()){
      if (orderEnum.getStateName().equals(stateName)){
        return orderEnum;
      }
    }
    return null;
  }

  private int orderState;
  private String stateName;

  public int getOrderState() {
    return orderState;
  }

  public String getStateName() {
    return stateName;
  }
}
