package com.ma.bookticket.service;

import com.ma.bookticket.pojo.User;
import org.springframework.stereotype.Service;
/**
 * User的服务器类
 * @author yong
 * @date 2021/1/18 17:01
 */

@Service
public interface UserService {

    /**
     * 通过登陆名查找用户，不存在则返回null
     * @param user_login_name
     * @author yong
     * @date 2021/1/19 14:38
     * @return com.ma.bookticket.pojo.User
     */
    public User selecOneByname(String user_login_name);

    /**
     * 新增一个用户
     * @param user 用户
     * @author yong
     * @date 2021/1/26 20:24
     * @return boolean
     */
    public boolean addOne(User user);

    /**
     * 修改一个用户的信息
     * @param user 用户
     * @author yong
     * @date 2021/1/26 20:26
     * @return int
     */
    public int update(User user);
}
