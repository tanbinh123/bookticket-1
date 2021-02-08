package com.ma.bookticket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@MapperScan("com.ma.bookticket.mapper")
@EnableTransactionManagement(proxyTargetClass = true) //开启事务支持
public class BookticketApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookticketApplication.class, args);
    }

}
