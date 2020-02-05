package cn.movie.robot.utils;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author Wuxiaoyi
 * @date 2020/2/5
 */
public class CopyUtil {
  public static void copyProperty(Object dest, Object origin){
    try {
      PropertyUtils.copyProperties(dest, origin);
    } catch (Exception e){

    }
  }
}
