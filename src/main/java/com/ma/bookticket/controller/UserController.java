package com.ma.bookticket.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.ma.bookticket.pojo.User;
import com.ma.bookticket.service.MailService;
import com.ma.bookticket.service.UserService;
import com.ma.bookticket.utils.KaptchaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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
     * @param request 请求
     * @param response 返回
     * @author yong
     * @date 2021/1/18 16:55
     * @return void
     */
    @GetMapping("/loginValidateCode")
    public void loginValidateCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        KaptchaUtils.validateCode(request,response,kaptchaProducer,LOGIN_VALIDATE_CODE);
    }
    /**
     * 登陆校验（包括验证码）
     *
     * @param validateCode 用户输入的验证码
     * @param username  用户名
     * @param password  密码
     * @param session   HttpSession
     * @param attributes RedirectAttributes 用于在重定向后页面获取参数
     * @author yong
     * @date 2021/1/18 17:07
     * @return java.lang.String
     */

    @PostMapping("/login")
    public String login(@RequestParam("validateCode")String validateCode,
                        @RequestParam String username,
                        @RequestParam String password,
                        HttpSession session, RedirectAttributes attributes){
        String loginValidateCode =(String) session.getAttribute(LOGIN_VALIDATE_CODE);
        User user = userService.selecOneByname(username);
        if (validateCode==null){
            attributes.addFlashAttribute("message","请输入验证码");
            return "redirect:/login";
        }else if (!validateCode.equals(loginValidateCode)){
            attributes.addFlashAttribute("message","验证码错误");
            return "redirect:/login";
        }else {
            if(user == null){
                attributes.addFlashAttribute("message","用户不存在");
                return "redirect:/login";
            }else if(user.getUser_password().equals(password)){
                user.setUser_password(null);
                session.setAttribute("username",username);
                return "redirect:/";
            }else{
                attributes.addFlashAttribute("message","用户名或密码错误");
                return "redirect:/login";
            }
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
                           RedirectAttributes attributes,HttpSession session) {
        String message="";  //提供辅助性信息
        String vefication_code=(String)session.getAttribute(REGISTER_VEFICATION_CODE);
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
        User user=new User();
        user.setUser_login_name(username);
        user.setUser_password(password);
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
        session.removeAttribute("username");
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
