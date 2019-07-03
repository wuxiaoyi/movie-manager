package cn.movie.robot.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @author Wuxiaoyi
 * @date 2019/6/28
 */

@EnableAsync
@Configuration
public class ThreadPoolConfig {

  private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("task-pool-%d").build();

  private final static Integer CORE_POOL_SIZE = 10;

  private final static Integer MAXIMUM_POOL_SIZE = 10;

  private final static Integer BLOCK_QUEUE_SIZE = Integer.MAX_VALUE;

  private final static Long KEEP_ALIVE_TIME = 10L;

  @Bean
  public ThreadPoolExecutor taskExecutor() {
    return new ThreadPoolExecutor(CORE_POOL_SIZE,
        MAXIMUM_POOL_SIZE,
        KEEP_ALIVE_TIME,
        TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(BLOCK_QUEUE_SIZE),
        threadFactory,
        new ThreadPoolExecutor.AbortPolicy());
  }
}
