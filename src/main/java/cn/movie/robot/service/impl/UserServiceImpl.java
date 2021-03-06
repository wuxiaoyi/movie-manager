package cn.movie.robot.service.impl;

import cn.movie.robot.common.Constants;
import cn.movie.robot.dao.UserRepository;
import cn.movie.robot.model.Provider;
import cn.movie.robot.model.User;
import cn.movie.robot.service.IUserService;
import cn.movie.robot.utils.CopyUtil;
import cn.movie.robot.vo.common.Result;
import cn.movie.robot.vo.req.SignUpVo;
import cn.movie.robot.vo.req.UserVo;
import cn.movie.robot.vo.resp.PageBean;
import cn.movie.robot.vo.resp.UserRespVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
  public Result queryAll(Pageable pageable) {
    Page<User> userPage = userRepository.findAll(pageable);
    PageBean<UserRespVo> userPageBean = new PageBean<>(
        userPage.getTotalElements(),
        userPage.getTotalPages(),
        dealUserList(userPage.getContent())
    );
    return Result.succ(userPageBean);
  }

  @Override
  public Result signUpKey() {
    String uuid = UUID.randomUUID().toString();
    String key = Constants.USER_SIGN_UP_KEY_PREFIX + uuid;
    redisTemplate.opsForValue().set(key, 1);
    redisTemplate.expire(key, 20, TimeUnit.MINUTES);
    return Result.succ(uuid);
  }

  @Override
  public Result signUp(SignUpVo signUpVo) {
    User existUser = userRepository.findByEmail(signUpVo.getEmail());
    if (Objects.nonNull(existUser)){
      return Result.fail("此邮箱已存在");
    }

    existUser = userRepository.findByCellphone(signUpVo.getCellphone());
    if (Objects.nonNull(existUser)){
      return Result.fail("此手机号已存在");
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
  public Result updateState(Integer userId, int state) {
    User user = userRepository.getOne(userId);
    if (Objects.isNull(user)){
      return Result.error("此用户不存在");
    }
    user.setState(state);
    userRepository.save(user);
    return Result.succ();
  }

  @Override
  public Result resetPwd(Integer userId, String password) {
    User user = userRepository.getOne(userId);
    if (Objects.isNull(user)){
      return Result.error("用户不存在");
    }

    RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    String slat = randomNumberGenerator.nextBytes().toHex();
    String encryptPwd = new SimpleHash("md5", password,
        ByteSource.Util.bytes(slat), 1).toHex();
    user.setPassword(encryptPwd);
    user.setPasswordSlat(slat);
    userRepository.save(user);

    return Result.succ();
  }

  @Override
  public Result update(Integer userId, UserVo userVo) {
    User user = userRepository.getOne(userId);
    if (Objects.isNull(user)){
      return Result.error("用户不存在");
    }

    user.setEmail(userVo.getEmail());
    user.setCellphone(userVo.getCellphone());
    user.setName(userVo.getName());
    userRepository.save(user);
    return Result.succ();
  }

  @Override
  public Result queryAll() {
    return Result.succ(dealUserList(userRepository.findAll()));
  }

  private List<UserRespVo> dealUserList(List<User> userList){
    List<UserRespVo> userRespVoList = new ArrayList<>();
    for (User user : userList){
      UserRespVo userRespVo = new UserRespVo();
      CopyUtil.copyProperty(userRespVo, user);
      userRespVoList.add(userRespVo);
    }
    return userRespVoList;
  }
}
