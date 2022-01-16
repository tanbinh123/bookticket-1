package com.ma.bookticket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@MapperScan("com.ma.bookticket.mapper")
@EnableTransactionManagement(proxyTargetClass = true) //开启事务支持
public class BookticketApplication {

    // 用于静态获取bean
    private static ConfigurableApplicationContext applicationContext;
    public static void main(String[] args) {
        applicationContext = SpringApplication.run(BookticketApplication.class, args);
    }

}
