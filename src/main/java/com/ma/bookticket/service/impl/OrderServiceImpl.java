package com.ma.bookticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ma.bookticket.mapper.LineMapper;
import com.ma.bookticket.mapper.OrderMapper;
import com.ma.bookticket.mapper.TripsMapper;
import com.ma.bookticket.pojo.Line;
import com.ma.bookticket.pojo.Orders;
import com.ma.bookticket.pojo.Trips;
import com.ma.bookticket.service.OrderService;
import com.ma.bookticket.service.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 订单服务的实现类
 *
 * @author yong
 * @date 2021/1/24 16:16
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TripsMapper tripsMapper;
    @Autowired
    private LineMapper lineMapper;
    @Autowired
    private TripsService tripsService;

    @Override
    @Transactional
    public int saveOne(Orders order) {
        //生成订单
        orderMapper.insert(order);
        //减少车票数量
        int trips_id = order.getOrder_trips_id();
        int order_seat_level = order.getOrder_seat_level();
        if(order_seat_level==1)
            tripsMapper.decrease_first_seat(trips_id);
        else
            tripsMapper.decrease_second_seat(trips_id);
        return 1;
    }

    @Override
    public List<Orders> getOrderListByUser(int user_id) {

        List<Orders> ordersList = orderMapper.selectList(
                new QueryWrapper<Orders>().eq("order_user_id", user_id).orderByDesc("order_create_time"));
        if(ordersList!=null&&ordersList.size()>0) {
            ordersList.forEach(order -> {
                int trips_id=order.getOrder_trips_id();
                Trips trips = tripsMapper.getOneByIdForOrder(trips_id);     //已逻辑删除的车次也会获取到
                order.setOrder_train_name(trips.getTrips_train_name());     //获得列车名

                Date date=trips.getTrips_start_time();
                if(date.getTime()<=new Date().getTime()) {
                    order.setOrder_status(1);                               //设置已发车
                    orderMapper.updateById(order);
                }

                order.setStart_date(date);                                  //设置发车日期

                int line_id=trips.getTrips_line_id();
                Line line = lineMapper.selectById(line_id);
                order.setStart_city(line.getLine_start_station_name());     //设置出发和达到城市
                order.setEnd_city(line.getLine_end_station_name());

            });
        }


        return ordersList;
    }

    @Override
    @Transactional
    public int refundTicket(int order_id) {
        Orders order = orderMapper.selectById(order_id);
        order.setOrder_status(2);
        orderMapper.updateById(order);  //更改订单的状态为退票状态

        int trips_id=order.getOrder_trips_id();
        int seat_level=order.getOrder_seat_level();
        if(seat_level==1)                       //相应坐席车票数量+1
            tripsMapper.increas_first_seat(trips_id);
        else
            tripsMapper.increas_second_seat(trips_id);
        return 1;
    }

    @Override
    public Orders getOneById(int order_id) {
        return orderMapper.selectById(order_id);
    }

    @Override
    @Transactional
    public int changing_order(int order_id, int trips_id) {
        Orders old_order = getOneById(order_id);
        int seat_level=old_order.getOrder_seat_level();
        old_order.setOrder_status(3);                   //原订单状态为已经改签状态
        orderMapper.updateById(old_order);              //修改

        //对原车次的车票数量+1
        Integer old_trips_id = old_order.getOrder_trips_id();
        if(seat_level==1)
            tripsMapper.increas_first_seat(old_trips_id);
        else
            tripsMapper.increas_second_seat(old_trips_id);

        //新车次车票数量-1
        Trips new_trips = tripsService.getOneById(trips_id);
        Float new_trips_price;
        if(seat_level==1) {
            new_trips_price = new_trips.getTrips_first_seat_price();
            tripsMapper.decrease_first_seat(trips_id);
        } else {
            new_trips_price=new_trips.getTrips_second_seat_price();
            tripsMapper.decrease_second_seat(trips_id);
        }

        Orders new_order=old_order;
        new_order.setOrder_id(null);
        new_order.setOrder_trips_id(trips_id);
        new_order.setOrder_status(0);   //  新订单状态为创建
        new_order.setOrder_create_time(null);
        new_order.setOrder_update_time(null);
        new_order.setOrder_price(new_trips_price);
        orderMapper.insert(new_order);
        return 1;
    }
}
