package cn.movie.robot.shiro;

import cn.movie.robot.dao.UserRepository;
import cn.movie.robot.model.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
public class ShiroRealm extends AuthorizingRealm {

  @Resource
  private UserRepository userRepository;

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    return null;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    String email = (String) token.getPrincipal();
    User user = userRepository.findByEmail(email);
    if (Objects.isNull(user)){
      return null;
    }
    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
        user,
        user.getPassword(),
        ByteSource.Util.bytes(user.getPasswordSlat()),
        getName()
    );

    return authenticationInfo;
  }
}
