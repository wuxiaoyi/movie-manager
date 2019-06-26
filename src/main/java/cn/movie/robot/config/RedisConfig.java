package cn.movie.robot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
@Configuration
public class RedisConfig {
  @Resource
  private RedisTemplate redisTemplate;

  @Bean
  public RedisTemplate getRedisTemplate() {
    StringRedisSerializer stringSerializer = new StringRedisSerializer();
    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);

    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
    redisTemplate.setValueSerializer(serializer);
    redisTemplate.setHashValueSerializer(serializer);
    return redisTemplate;
  }
}
