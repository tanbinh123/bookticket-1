package com.ma.bookticket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.ma.bookticket.mapper")
public class BookticketApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookticketApplication.class, args);
    }

}
