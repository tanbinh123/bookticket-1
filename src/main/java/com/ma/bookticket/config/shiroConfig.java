package com.ma.bookticket.config;

import com.ma.bookticket.common.DbRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置类
 *
 * @author yong
 * @date 2021/1/29 23:01
 */
@Configuration
public class shiroConfig {

    /**
     * 注入 Shiro 过滤器
     * @author yong
     * @date 2021/1/30 16:47
     * @return org.apache.shiro.spring.web.ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        // 定义 shiroFactoryBean
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        // 设置自定义的 securityManager
        shiroFilter.setSecurityManager(mySecurityManager());
        //设置默认登录的 URL，身份认证失败会访问该 URL
        shiroFilter.setLoginUrl("/login");
        // 设置成功之后要跳转的链接
        shiroFilter.setSuccessUrl("/");
        // LinkedHashMap 是有序的，进行顺序拦截器配置
        Map<String,String> filterChainMap = new LinkedHashMap<>();
        // 配置可以匿名访问的地址，可以根据实际情况自己添加，放行一些静态资源等，anon 表示放行
       1 filterChainMap.put("/css/**", "anon");
        filterChainMap.put("/images/**", "anon");
        filterChainMap.put("/js/**", "anon");
        filterChainMap.put("/", "anon");
        filterChainMap.put("/getTrips", "anon");
        // 登录 URL 放行
        filterChainMap.put("/login", "anon");
        // 注册 URL 放行
        filterChainMap.put("/register", "anon");
        // 以“/user” 开头的用户需要身份认证，authc 表示要进行身份认证
        filterChainMap.put("/user/**", "authc");
        // 配置 logout 过滤器
        filterChainMap.put("/logout", "logout");

        shiroFilter.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilter;
    }


    /**
     * 注入安全管理器
     * @author yong
     * @date 2021/1/29 23:35
     * @return java.lang.SecurityManager
     */
    @Bean
    public DefaultWebSecurityManager mySecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }
    /**
     *
     * 注入自定义的Realm
     * @author yong
     * @date 2021/1/29 23:33 
     * @return com.ma.bookticket.common.DbRealm
     */
    @Bean
    public DbRealm myRealm() {
        return new DbRealm();
    }

    /**
     * 会话管理器
     * @author yong
     * @date 2021/1/30 17:03
     * @return org.apache.shiro.web.session.mgt.DefaultWebSessionManager
     */
    @Bean
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager sessionManager=new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(3600000);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }
}
