package cn.movie.robot.config;

import cn.movie.robot.utils.JacksonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wuxiaoyi
 * @date 2019/6/28
 */

@Configuration
public class JacksonConfig {

  @Bean
  public MappingJackson2HttpMessageConverter converter() {
    MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
    List supportedMediaTypes = new ArrayList();
    supportedMediaTypes.add(new MediaType("application", "json"));
    jsonMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
    jsonMessageConverter.setObjectMapper(JacksonUtil.getObjectMapper());
    return jsonMessageConverter;
  }
}