package cn.movie.robot.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;

/**
 * @author 小柯
 * @date 2018/3/13
 */
public class NumberUtil {

  /**
   * 正数
   *
   * @param num
   * @return boolean
   */
  public static boolean isPositive(Integer num) {
    return isGreaterThan(num, 0);
  }

  /**
   * 都是正数
   *
   * @param nums
   * @return boolean
   */
  public static boolean arePositive(Integer... nums) {
    return !ArrayUtils.isEmpty(nums) && Arrays.stream(nums).allMatch(NumberUtil::isPositive);
  }

  /**
   * 大于
   *
   * @param x
   * @param y
   * @return boolean
   */
  public static boolean isGreaterThan(Integer x, Integer y) {
    return ObjectUtils.compare(x, y) > 0;
  }

  /**
   * 小于
   *
   * @param x
   * @param y
   * @return boolean
   */
  public static boolean isLessThan(Integer x, Integer y) {
    return ObjectUtils.compare(x, y) < 0;
  }

  /**
   * 数量限制 不超过maxCount
   *
   * @param count
   * @param maxCount
   * @return boolean
   */
  public static int getMaxCount(Integer count, Integer maxCount) {
    if (count == null) {
      return 0;
    }
    if (count.intValue() > maxCount.intValue()) {
      return maxCount;
    }
    return count;
  }

  private NumberUtil() {
  }
}
