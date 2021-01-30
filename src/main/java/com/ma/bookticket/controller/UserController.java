package com.ma.bookticket.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.ma.bookticket.pojo.User;
import com.ma.bookticket.service.MailService;
import com.ma.bookticket.service.UserService;
import com.ma.bookticket.utils.KaptchaUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Random;

/**
 * 控制用户相关,包括登陆、注册、注销等
 *
 * @author yong
 * @date 2021/1/18 16:20
 */
@Controller
public class UserController {
    /**
     * 登录验证码SessionKey
     * 是HttpSession的一个属性名，该属性的值为验证码生成的验证码文本
     */
    public static final String LOGIN_VALIDATE_CODE = "login_validate_code";//登陆所用的验证码
    public static final String REGISTER_VEFICATION_CODE="register_vefication_code";//注册所用的验证码

    @Autowired
    private DefaultKaptcha kaptchaProducer;
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;


    /**
     * 跳转到登陆界面
     * @author yong
     * @date 2021/1/19 20:15
     * @return java.lang.String
     */

    @GetMapping("/login")
    public String Login(){
        return "user/login";
    }
    /**
     * 跳转到注册界面
     * @author yong
     * @date 2021/1/19 20:16
     * @return java.lang.String
     */

    @GetMapping("/register")
    public String register() {
        return "user/register";
    }
    /**
     * 生成登陆码验证图片，并在reponse里添加验证码图片等信息（KaptchaUtils里可查看具体步骤）
     * 此时HttpSession里也存放了验证码文本信息
     * @param response 返回
     * @author yong
     * @date 2021/1/18 16:55
     * @return void
     */
    @GetMapping("/loginValidateCode")
    public void loginValidateCode(HttpServletResponse response) throws Exception {
        KaptchaUtils.validateCode(response,kaptchaProducer,LOGIN_VALIDATE_CODE);
    }
    /**
     * 登陆校验（包括验证码）
     *
     * @param validateCode 用户输入的验证码
     * @param username  用户名
     * @param password  密码
     * @param attributes RedirectAttributes 用于在重定向后页面获取参数
     * @author yong
     * @date 2021/1/18 17:07
     * @return java.lang.String
     */

    @PostMapping("/login")
    public String login(@RequestParam("validateCode")String validateCode,
                        @RequestParam String username,
                        @RequestParam String password,
                        RedirectAttributes attributes){
        // 根据用户名和密码创建 Token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 获取 subject 认证主体
        Subject currentUser = SecurityUtils.getSubject() ;
        Session session = currentUser.getSession() ;

        //获取session里的登陆验证码
        String loginValidateCode =(String) session.getAttribute(LOGIN_VALIDATE_CODE);
        String message="" ;  //存储用于传递给跳转页面的信息

        if (validateCode==null||validateCode.length()==0||validateCode.equals("")){
            message="请输入验证码" ;
        }else if (!validateCode.equals(loginValidateCode)){
            message="验证码错误" ;
        }else {
            try {
                // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
                // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
                // 所以这一步在调用login(token)方法时,它会走到xxRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
                currentUser.login(token);
            } catch (UnknownAccountException e) {
                token.clear();
                message="用户不存在" ;
            } catch (IncorrectCredentialsException e) {
                token.clear();
                message="用户名或密码错误" ;
            }
        }
        if(!message.equals("")) {
            attributes.addFlashAttribute("message",message);
            return "redirect:/login";
        }else {
            return "redirect:/" ;
        }
    }
    /**
     * 先校验注册时用户所填的信息是否非法，如果合法再去数据库中查找是否有该用户，非法或用户已经存在的要提供相应的提示信息
     * 若合法且无用户，可保存该用户
     * @param username 用户登陆名
     * @param password 密码
     * @param email 邮箱号
     * @param verification_code 邮箱验证码
     * @param attributes 用于在重定向后页面获取参数
     * @author yong
     * @date 2021/1/19 23:49
     * @return java.lang.String
     */

    @PostMapping("/register")
    public String register(@RequestParam String username,@RequestParam String password,
                           @RequestParam("email") String email,@RequestParam("verification_code") String verification_code,
                           RedirectAttributes attributes) {
        String message="";  //提供辅助性信息
        String vefication_code=(String) SecurityUtils.getSubject().getSession().getAttribute(REGISTER_VEFICATION_CODE);
        boolean flag=true;
        if(!StringUtils.hasText(username)||!StringUtils.hasText(password)) {
            message="用户名和密码不能为空";
            flag=false;
        }else if(flag&&!StringUtils.hasText(email)) {
            message="邮箱不能为空！！！";
            flag=false;
        }else if(!verification_code.equals(vefication_code)) {
            message="验证码错误！！！";
            flag=false;
        }
        else if(flag&&userService.selecOneByname(username)!=null) {
            message="用户已注册";
        }
        if(!message.equals("")) {
            attributes.addFlashAttribute("message",message);
            return "redirect:/register";
        }

        String salt = new SecureRandomNumberGenerator().nextBytes().toHex(); //随机生成盐值
        String pwd = new Md5Hash(password,salt,1).toHex(); //生成的密文，使用md5算法对明文与盐值的组合进行了一次加密

        User user=new User();
        user.setUser_login_name(username);
        user.setUser_password(pwd);
        user.setUser_salt(salt);
        user.setUser_email(email);
        userService.addOne(user);
        attributes.addFlashAttribute("success_message","恭喜注册成功！！！！");
        return "redirect:/login";
    }

    /**
     * 注销，并跳转到用户登陆界面
     * @param session HttpSession
     * @author yong
     * @date 2021/1/21 15:09
     * @return java.lang.String
     */

    @GetMapping("/logout")
    public String logout(HttpSession session){
        // 此处有坑： 退出登录，其实不用实现任何东西，只需要保留这个接口即可，也不可能通过下方的代码进行退出
        // SecurityUtils.getSubject().logout();
        // 因为退出操作是由Shiro控制的
        return "redirect:/login";
    }


    /**
     * 随机生成六位验证码，并发送到指定邮箱
     * @param email 邮箱号
     * @author yong
     * @date 2021/1/26 2:45
     * @return java.lang.String
     */
    @RequestMapping("/getCheckCode")
    @ResponseBody
    public String getCheckCode(@RequestParam String email,RedirectAttributes attributes,HttpSession session) {
        if(!StringUtils.hasText(email)) {
            attributes.addFlashAttribute("message","验证码为空，请输入!!!");
            return "redirect:/register";
        }
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String Subject="火车票订票系统验证码";
        String Text="你的验证码为：" + checkCode;
        String To=email;
        mailService.sendSimpleMail(To,Subject,Text);
        session.setAttribute(REGISTER_VEFICATION_CODE,checkCode);//保存验证码以便验证
        return "请查看邮箱收到的验证码！！！！";
    }
}
