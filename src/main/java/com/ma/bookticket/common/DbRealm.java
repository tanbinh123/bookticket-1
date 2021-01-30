package com.ma.bookticket.common;

import com.ma.bookticket.pojo.User;
import com.ma.bookticket.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基于数据库操作的自定义 Realm
 *
 * @author yong
 * @date 2021/1/29 23:07
 */
public class DbRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 进行授权操作
     *
     * @param principalCollection
     * @author yong
     * @date 2021/1/29 23:11
     * @return org.apache.shiro.authz.AuthorizationInfo
     */

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 进行用户认证操作
     *
     * @param token 用户信息令牌
     * @author yong
     * @date 2021/1/29 23:12
     * @return org.apache.shiro.authc.AuthenticationInfo
     */

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 根据 Token 获取用户名
        String username =(String)token.getPrincipal();
        // 根据用户名从数据库中查询该用户
        User user = userService.selecOneByname(username);
        if(user != null) {
            // 把当前用户名存到 Session 中
            SecurityUtils.getSubject().getSession().setAttribute("username",username);
            // 传入用户名和密码进行身份认证，并返回认证信息
            AuthenticationInfo  authcInfo = new SimpleAuthenticationInfo(user.getUser_login_name(), user.getUser_password(), "DbRealm");
            return authcInfo;
        }else {
            return null;
        }
    }
}
