package cn.movie.robot.shiro;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.UserRepository;
import cn.movie.robot.model.Permission;
import cn.movie.robot.model.Role;
import cn.movie.robot.model.User;
import cn.movie.robot.service.IPermissionService;
import cn.movie.robot.vo.common.SessionUser;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
public class ShiroRealm extends AuthorizingRealm {

  @Resource
  private UserRepository userRepository;

  @Autowired
  private IPermissionService permissionService;

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    Object o = principalCollection.getPrimaryPrincipal();
    User user = new User();
    try {
      BeanUtils.copyProperties(user, o);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    List<Role> roles = permissionService.queryRoleByUser(user);
    List<Permission> permissions = permissionService.queryPermissionByUser(user);

    info.addRoles(roles.stream().map(Role::getName).collect(Collectors.toList()));
    info.addStringPermissions(permissions.stream().map(Permission::getName).collect(Collectors.toList()));
    return info;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    String email = (String) token.getPrincipal();
    User user = userRepository.findByEmail(email);
    if (Objects.isNull(user)){
      return null;
    }

    if (user.getState() == Constants.USER_STATE_FORBIDDEN){
      throw new LockedAccountException();
    }

    SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
        user,
        user.getPassword(),
        ByteSource.Util.bytes(user.getPasswordSlat()),
        getName()
    );

    return info;
  }
}
