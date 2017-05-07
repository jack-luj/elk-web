package com.github.jackl.elk;


import com.github.jackl.elk.shiro.SessionRedisImpl;
import com.github.jackl.elk.shiro.ShiroRealmImpl;
import com.github.jackl.elk.utils.UnauthorizedExceptionHandler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by jackl on 2017/2/13.
 */
@Configuration
public class ShiroConfiguration {

    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

    @Bean(name = "ShiroRealmImpl")
    public ShiroRealmImpl getShiroRealm() {
        return new ShiroRealmImpl();
    }

    @Bean(name = "SessionRedisImpl")
    public SessionRedisImpl getSessionRedisImpl() {
        return new SessionRedisImpl();
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(getShiroRealm());
        dwsm.setSessionManager(getDefaultWebSessionManager());
       // dwsm.setCacheManager(getEhCacheManager());
        return dwsm;
    }

    @Bean(name = "sessionManager")
    public DefaultWebSessionManager getDefaultWebSessionManager() {
        DefaultWebSessionManager dwsm = new DefaultWebSessionManager();
//        dwsm.setSessionDAO(new SessionRedisImpl());
        dwsm.setSessionDAO(getSessionRedisImpl());
        dwsm.setSessionIdCookie(new SimpleCookie("SESSIONID"));//sessionid-->cookie name
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(getDefaultWebSecurityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean
                .setSecurityManager(getDefaultWebSecurityManager());
        shiroFilterFactoryBean.setLoginUrl("../login.html");
        shiroFilterFactoryBean.setSuccessUrl("index.html");
        filterChainDefinitionMap.put("/login.html", "anon");
        filterChainDefinitionMap.put("../index.html", "authc");
        filterChainDefinitionMap.put("/sys/*", "authc");
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean
                .setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    //自定义的异常处理，出现无权限异常时返回,{"message":"error message",status":403}
    @Bean
    public UnauthorizedExceptionHandler getUnauthorizedExceptionHandler(){
        return new UnauthorizedExceptionHandler();
    }


}