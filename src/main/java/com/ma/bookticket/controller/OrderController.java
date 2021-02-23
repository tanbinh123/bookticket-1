package com.ma.bookticket.controller;

import com.ma.bookticket.pojo.Orders;
import com.ma.bookticket.pojo.Trips;
import com.ma.bookticket.pojo.User;
import com.ma.bookticket.service.OrderService;
import com.ma.bookticket.service.TripsService;
import com.ma.bookticket.service.UserService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单控制
 *
 * @author yong
 * @date 2021/1/24 16:14
 */
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private TripsService tripsService;

    public static final Logger logger= LoggerFactory.getLogger(OrderController.class);
    /**
     * 预定车票，生成相应的订单，并且车票数量相应减少
     *
     * @param trips_id 车次编号
     * @param request 请求信息
     * @author yong
     * @date 2021/1/27 21:17
     * @return java.lang.String
     */

    @PostMapping("/user/add_order/{trips_id}")
    public String addOne(@PathVariable(value = "trips_id") Integer trips_id, HttpServletRequest request) {

        logger.info("----------------------预定车次："+trips_id+"，生成相应的订单，并且车票数量相应减少-----------------------------");

        Session session= SecurityUtils.getSubject().getSession();

        String passenger_name = request.getParameter("passenger_name");
        String seat_level = request.getParameter("seat_level");
        String passenger_identity_num =request.getParameter("passenger_identity_num");
        String linkman_name =request.getParameter("linkman_name");
        String linkman_phone =request.getParameter("linkman_phone");
        User user =(User) session.getAttribute("user");
        int user_id=user.getUser_id();

        if(StringUtils.hasText(passenger_name) && StringUtils.hasText(passenger_identity_num)
                && StringUtils.hasText(seat_level) ) {

            //获取票价
            char[] chars = seat_level.toCharArray();
            int index=0;//数字字符的开始位置
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] >= 48 && chars[i] <= 57) {
                    index = i;
                    break;
                }
            }
            float order_price=Float.parseFloat(seat_level.substring(index));
            //获取坐席级别
            int seat_level_flag=1;
            if(seat_level.contains("二等座"))
                seat_level_flag=2;

            Orders order = new Orders();
            order.setOrder_passenger_name(passenger_name);
            order.setOrder_seat_level(seat_level_flag);
            order.setOrder_passenger_identity_num(passenger_identity_num);
            order.setOrder_linkman_name(linkman_name);
            order.setOrder_linkman_phone(linkman_phone);
            order.setOrder_price(order_price);
            order.setOrder_status(0);   //初始为创建状态
            order.setOrder_trips_id(trips_id);
            order.setOrder_user_id(user_id);

            if(orderService.saveOne(order)==2);     //生成订单并减少车座数量
            return "redirect:/user/ToUserCenter";
        }
        return "/default/index";
    }
    /**
     * 进行退票操作，成功就返回订单列表
     * @param order_id 订单编号
     * @author yong
     * @date 2021/1/28 15:39
     * @return java.lang.String
     */

    @GetMapping("/user/refundTicket/{order_id}")
    public String refundTicket(@PathVariable(value = "order_id") Integer order_id) {

        logger.info("---------------进行退票操作,订单号为"+order_id+"-----------------------");
        orderService.refundTicket(order_id);
        return "redirect:/user/ToOrderList";
    }

    /**
     * 跳转到改签可选的车次页面
     *
     * @param order_id 要改签的订单编号
     * @param model 给页面传递参数
     * @author yong
     * @date 2021/1/28 16:52
     * @return java.lang.String
     */

    @GetMapping("/user/toChangingTicket/{order_id}")
    public String toChangingTicket(@PathVariable(value = "order_id") Integer order_id, Model model) {

        logger.info("------------------跳转到改签可选的车次页面,订单号为"+order_id+"---------------------");
        List<Trips> tripsList = tripsService.getChangingTrips(order_id);
        model.addAttribute("tripsList",tripsList);
        model.addAttribute("order_id",order_id);
        return "user/change_ticket";
    }

    /**
     * 进行改签操作
     *
     * @param trips_id 改签选择的新车次编号
     * @param order_id 原订单编号
     * @author yong
     * @date 2021/1/31 16:40
     * @return java.lang.String
     */

    @GetMapping("/user/changing_order")
    public String changing_order(@RequestParam("trips_id") Integer trips_id,
                                 @RequestParam("order_id") Integer order_id ) {

        logger.info("----------------进行改签操作,改签的订单号为"+order_id+"，选中改签的车次号为"+trips_id+"---------------");
        orderService.changing_order(order_id,trips_id);
        return "redirect:/user/ToOrderList";
    }
}
