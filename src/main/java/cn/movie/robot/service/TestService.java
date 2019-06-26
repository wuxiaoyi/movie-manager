package cn.movie.robot.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

/**
 * @author Wuxiaoyi
 * @date 2019/6/17
 */
public class TestService {

  public static void main(String[] args){
    RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    String slat = randomNumberGenerator.nextBytes().toHex();
    String encryptPwd = new SimpleHash("md5", "123456",
        ByteSource.Util.bytes(slat), 1).toHex();
    System.out.println(slat);
    System.out.println(encryptPwd);
  }
}
