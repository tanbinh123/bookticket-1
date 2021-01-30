package com.ma.bookticket.controller;

import com.ma.bookticket.pojo.Orders;
import com.ma.bookticket.pojo.Trips;
import com.ma.bookticket.pojo.User;
import com.ma.bookticket.service.OrderService;
import com.ma.bookticket.service.TripsService;
import com.ma.bookticket.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    /**
     * 预定车票，生成相应的订单，并且车票数量相应减少
     *
     * @param trips_id 车次编号
     * @param request 请求信息
     * @param session HttpSession
     * @author yong
     * @date 2021/1/27 21:17
     * @return java.lang.String
     */

    @PostMapping("/user/add_order/{trips_id}")
    public String addOne(@PathVariable(value = "trips_id") Integer trips_id, HttpServletRequest request, HttpSession session) {
        String passenger_name = request.getParameter("passenger_name");
        String seat_level = request.getParameter("seat_level");
        String passenger_identity_num =request.getParameter("passenger_identity_num");
        String linkman_name =request.getParameter("linkman_name");
        String linkman_phone =request.getParameter("linkman_phone");
        String username =(String) session.getAttribute("username");
        User user = userService.selecOneByname(username);
        int user_id=user.getUser_id();
        //获取票价
        char[] chars = seat_level.toCharArray();
        int index=0;//数字字符的开始位置
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 48 && chars[i] <= 57) {
                index = i;
                break;
            }
        }
        double order_price=Double.parseDouble(seat_level.substring(index));
        //获取坐席级别
        int seat_level_flag=1;
        if(seat_level != null&&seat_level.contains("二等座"))
            seat_level_flag=2;

        Orders order = new Orders();
        order.setOrder_passenger_name(passenger_name==null?"":passenger_name);//数据库中是非空字段，防止插入异常
        order.setOrder_seat_level(seat_level_flag);
        order.setOrder_passenger_identity_num(passenger_identity_num==null?"":passenger_identity_num);//数据库中是非空字段，防止插入异常
        order.setOrder_linkman_name(linkman_name);
        order.setOrder_linkman_phone(linkman_phone);
        order.setOrder_price(order_price);
        order.setOrder_status(0);   //初始为创建状态
        order.setOrder_trips_id(trips_id);
        order.setOrder_user_id(user_id);

        if(orderService.saveOne(order)==1);     //生成订单并减少车座数量
            return "redirect:/";
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
        orderService.refundTicket(order_id);
        return "redirect:/user/ToOrderList";
    }

    /**
     *
     *
     * @param order_id 要改签的订单编号
     * @param model 给页面传递参数
     * @author yong
     * @date 2021/1/28 16:52
     * @return java.lang.String
     */

    @GetMapping("/user/toChangingTicket/{order_id}")
    public String toChangingTicket(@PathVariable(value = "order_id") Integer order_id, Model model) {
        List<Trips> tripsList = tripsService.getChangingTrips(order_id);
        model.addAttribute("tripsList",tripsList);
        model.addAttribute("order_id",order_id);
        return "user/change_ticket";
    }

    @GetMapping("/user/changing_order")
    public String changing_order(@RequestParam("trips_id") Integer trips_id,
                                 @RequestParam("order_id") Integer order_id ) {
        orderService.changing_order(order_id,trips_id);
        return "redirect:/user/ToOrderList";
    }
}
