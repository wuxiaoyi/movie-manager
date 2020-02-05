package cn.movie.robot.utils;

import cn.movie.robot.model.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.util.ObjectUtils;

/**
 * @author Wuxiaoyi
 * @date 2020/2/4
 */
public class SessionUtil {
  public static User getCurrentUser(){
    Subject subject = SecurityUtils.getSubject();
    User user = new User();
    try {
      BeanUtils.copyProperties(user, subject.getPrincipal());
    } catch (Exception e) {
    }
    return user;
  }

  public static Integer getCurrentUserId(){
    Subject subject = SecurityUtils.getSubject();
    User user = new User();
    try {
      BeanUtils.copyProperties(user, subject.getPrincipal());
    } catch (Exception e) {
    }
    if (ObjectUtils.isEmpty(user.getId())){
      return 0;
    }
    return user.getId();
  }

  public static boolean hasPermission(String permission){
    return SecurityUtils.getSubject().isPermitted(permission);
  }
}
