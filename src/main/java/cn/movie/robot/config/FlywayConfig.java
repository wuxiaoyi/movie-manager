package cn.movie.robot.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;

/**
 * @author Wuxiaoyi
 * @date 2019/6/28
 */

@Configuration
public class FlywayConfig {

  @Resource
  private DataSource dataSource;

  @Bean
  public Flyway flyway() {
    Flyway flyway = new Flyway();
    flyway.setDataSource(dataSource);
    flyway.setBaselineOnMigrate(true);
    flyway.repair();
    flyway.migrate();
    flyway.setEncoding(StandardCharsets.UTF_8.name());
    return flyway;
  }
}
