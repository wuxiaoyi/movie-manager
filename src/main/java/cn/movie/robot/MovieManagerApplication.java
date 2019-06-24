package cn.movie.robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication
public class MovieManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(MovieManagerApplication.class, args);
  }
}
