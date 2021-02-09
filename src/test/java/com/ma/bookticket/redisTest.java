package com.ma.bookticket;

import com.ma.bookticket.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * redis测试
 *
 * @author yong
 * @date 2021/2/8 21:33
 */
@SpringBootTest
public class redisTest {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 测试插入带有过期时间的值，以及获取
     *
     * @author yong
     * @date 2021/2/8 21:42
     * @return void
     */

    @Test
    public void testSetAndGet() {
        if(redisUtils.set("test","Ok!",60)) {
            System.out.println("---------------------成功插入---------------------");
            System.out.println("---------------------插入的值为："+redisUtils.get("test")+"---------------------");
        }else
            System.out.println("---------------------插入发生异常---------------------");

    }

}
