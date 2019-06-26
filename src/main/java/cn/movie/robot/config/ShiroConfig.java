package cn.movie.robot.config;

import cn.movie.robot.shiro.SessionManager;
import cn.movie.robot.shiro.ShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Wuxiaoyi
 * @date 2019/6/25
 */
@Configuration
public class ShiroConfig {

  @Value("${shiro.redis.host}")
  private String redisHost;
  @Value("${shiro.redis.timeout}")
  private Integer redisTimeout;
  @Value("${shiro.redis.password}")
  private String redisPassword;
  @Value("${shiro.redis.database}")
  private Integer redisDatebase;

  @Bean
  public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager);
    //拦截器.
    Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
    //注意过滤器配置顺序 不能颠倒
    filterChainDefinitionMap.put("/session/logout", "anon");
    filterChainDefinitionMap.put("/session/login", "anon");
    filterChainDefinitionMap.put("/**", "authc");
    //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
    shiroFilterFactoryBean.setLoginUrl("/session/unauth");
    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    return shiroFilterFactoryBean;
  }

  @Bean
  public SecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealm(myShiroRealm());
    securityManager.setSessionManager(sessionManager());
    securityManager.setCacheManager(cacheManager());
    //注入记住我管理器
    securityManager.setRememberMeManager(rememberMeManager());
    return securityManager;
  }

  @Bean
  public SessionManager sessionManager() {
    SessionManager mySessionManager = new SessionManager();
    mySessionManager.setSessionDAO(redisSessionDAO());
    return mySessionManager;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
    return authorizationAttributeSourceAdvisor;
  }

  @Bean
  public ShiroRealm myShiroRealm() {
    ShiroRealm myShiroRealm = new ShiroRealm();
    myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
    return myShiroRealm;
  }

  @Bean
  public HashedCredentialsMatcher hashedCredentialsMatcher() {
    HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
    hashedCredentialsMatcher.setHashAlgorithmName("MD5");
    hashedCredentialsMatcher.setHashIterations(1);
    return hashedCredentialsMatcher;
  }

  /**
   * 配置shiro redisManager 使用的是shiro-redis开源插件
   *
   * @return
   */
  public RedisManager redisManager() {
    RedisManager redisManager = new RedisManager();
    redisManager.setHost(this.redisHost);
//    redisManager.setPort(this.redisPort);
    redisManager.setTimeout(this.redisTimeout);
//    redisManager.setPassword(this.redisPassword);
    redisManager.setDatabase(this.redisDatebase);
    return redisManager;
  }

  @Bean
  public RedisSessionDAO redisSessionDAO() {
    RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
    redisSessionDAO.setRedisManager(redisManager());
    redisSessionDAO.setKeyPrefix("shiro:user:");
    return redisSessionDAO;
  }

  @Bean
  public RedisCacheManager cacheManager() {
    RedisCacheManager redisCacheManager = new RedisCacheManager();
    redisCacheManager.setRedisManager(redisManager());
    redisCacheManager.setPrincipalIdFieldName("id");
    return redisCacheManager;
  }

  /**
   * 3.此处对应前端“记住我”的功能，获取用户关联信息而无需登录
   * @return
   */
  @Bean
  public SimpleCookie rememberMeCookie(){
    //这个参数是cookie的名称，对应前端的checkbox的name = remember
    SimpleCookie simpleCookie = new SimpleCookie("remember");
    simpleCookie.setMaxAge(259200);
    return simpleCookie;
  }

  @Bean
  public CookieRememberMeManager rememberMeManager(){
    CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
    cookieRememberMeManager.setCookie(rememberMeCookie());
    cookieRememberMeManager.setCipherKey(Base64.decode("one"));
    return cookieRememberMeManager;
  }
}
