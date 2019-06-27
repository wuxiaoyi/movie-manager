package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.UserRepository;
import cn.movie.robot.model.User;
import cn.movie.robot.service.IUserService;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.SignUpVo;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Wuxiaoyi
 * @date 2019/6/27
 */
@Service
public class UserServiceImpl implements IUserService {

  @Resource
  RedisTemplate redisTemplate;

  @Resource
  UserRepository userRepository;

  @Override
  public Result signUpKey() {
    String uuid = UUID.randomUUID().toString();
    String key = Constants.USER_SIGN_UP_KEY_PREFIX + uuid;
    redisTemplate.opsForValue().set(key, 1);
    redisTemplate.expire(key, 5, TimeUnit.MINUTES);
    return Result.succ(uuid);
  }

  @Override
  public Result signUp(SignUpVo signUpVo) {
    String signUpUuid = signUpVo.getSignUpKey();

    if (Objects.isNull(redisTemplate.opsForValue().get(Constants.USER_SIGN_UP_KEY_PREFIX + signUpUuid))){
      return Result.error("注册码5分钟有效,已过期");
    }

    User user = new User();
    user.setName(signUpVo.getName());
    user.setCellphone(signUpVo.getCellphone());
    user.setEmail(signUpVo.getEmail());
    RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    String slat = randomNumberGenerator.nextBytes().toHex();
    String encryptPwd = new SimpleHash("md5", signUpVo.getPassword(),
        ByteSource.Util.bytes(slat), 1).toHex();
    user.setPassword(encryptPwd);
    user.setPasswordSlat(slat);

    userRepository.save(user);

    return Result.succ();
  }

  @Override
  public Result forbiddenUser(Integer userId) {
    return null;
  }

  @Override
  public Result forgetPwdKey(String email) {
    return null;
  }
}
