package com.ma.bookticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ma.bookticket.mapper.UserMapper;
import com.ma.bookticket.pojo.User;
import com.ma.bookticket.service.UserService;
import com.ma.bookticket.utils.RedisUtils;
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
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public User selecOneByname(String user_login_name) {
        User user=(User)redisUtils.get("user"+user_login_name);     //查找缓存
        if(user!=null)
            return user;
        else {
            user=userMapper.selectOne(new QueryWrapper<User>().eq(true,"user_login_name",user_login_name));
            if(user!=null)
                redisUtils.set("user"+user_login_name,user);
            return user;
        }

    }
    @Override
    public boolean addOne(User user) {
        String user_login_name = user.getUser_login_name();
        if(userMapper.insert(user)!=0 ) {
            redisUtils.set("user"+user_login_name,user);   //放入redis缓存，并设置过期时间
            return true;
        }

        return false;
    }

    @Override
    public int update(User user)  {
        if(userMapper.updateById(user)!=0) {
            // 获取 subject 认证主体
            Subject currentUser = SecurityUtils.getSubject() ;
            Session session = currentUser.getSession() ;
            session.setAttribute("user",user);
            redisUtils.set("user"+user.getUser_login_name(),user);//更新缓存
            return 1;
        }
        return 0;
    }
}
