package com.ma.bookticket;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试密码经过md5算法加密后的结果
 *
 * @author yong
 * @date 2021/1/30 20:52
 */
@SpringBootTest
public class md5Test {
    @Test
    public void md51() {
        String password="ma";                               //输入的密码
        String salt="a08f998cfd031c440d15d889aff5b8f5";     //盐值
        String db="b47bcecb300c5d86f4ce5f42a87b9e05";       //数据库中的密码
        String res = new Md5Hash(password, salt, 1).toString();
        System.out.println("密码："+password+",盐值："+salt+",加密后结果为："+res);
        System.out.println("与数据库比较结果为"+res.equals(db));
    }
}
