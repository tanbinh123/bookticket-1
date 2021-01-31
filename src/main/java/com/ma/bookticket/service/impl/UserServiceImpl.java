package com.ma.bookticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ma.bookticket.mapper.UserMapper;
import com.ma.bookticket.pojo.User;
import com.ma.bookticket.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * userService的实现类
 *
 * @author yong
 * @date 2021/1/19 14:08
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selecOneByname(String user_login_name) {
        User user;
        user=userMapper.selectOne(new QueryWrapper<User>().eq(true,"user_login_name",user_login_name));
        return user;
    }
    @Override
    public boolean addOne(User user) {
        if(userMapper.insert(user)!=0 )
            return true;
        return false;
    }

    @Override
    public int update(User user) {
        // 获取 subject 认证主体
        Subject currentUser = SecurityUtils.getSubject() ;
        Session session = currentUser.getSession() ;
        session.setAttribute("user",user);
        return userMapper.updateById(user);
    }
}
