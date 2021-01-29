package com.ma.bookticket.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.ma.bookticket.handler.handler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 配置MybatisPlus的分页插件
 * @author yong
 * @date 2021/1/17 21:46
 */
@EnableTransactionManagement    //开启事务
@Configuration
@MapperScan("com.ma.bookticket.mapper")
public class MybatisPlusConfig {

    /**
     * 乐观锁
     * @author yong
     * @date 2021/1/17 21:54
     * @return com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor
     */
//    @Bean
//    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
//        return new OptimisticLockerInnerInterceptor();
//    }

    /**
     * 自动填充功能
     * @author yong
     * @date 2021/1/28 22:00
     * @return com.baomidou.mybatisplus.core.config.GlobalConfig
     */
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new handler());
        return globalConfig;
    }

}
