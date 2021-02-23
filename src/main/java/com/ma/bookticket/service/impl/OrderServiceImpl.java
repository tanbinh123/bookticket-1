package com.ma.bookticket.service.impl;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ma.bookticket.mapper.OrderMapper;
import com.ma.bookticket.pojo.Line;
import com.ma.bookticket.pojo.Orders;
import com.ma.bookticket.pojo.Trips;
import com.ma.bookticket.service.LineService;
import com.ma.bookticket.service.OrderService;
import com.ma.bookticket.service.TripsService;
import com.ma.bookticket.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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
    private LineService lineService;
    @Autowired
    private TripsService tripsService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int saveOne(Orders order) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("订票操作！");
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);

        //生成订单
        int count=orderMapper.insert(order);
        //减少车票数量
        int trips_id = order.getOrder_trips_id();
        int order_seat_level = order.getOrder_seat_level();
        if(order_seat_level==1)
            count+=tripsService.decrease_first_seatnum(trips_id);
        else
            count+=tripsService.decrease_second_seatnum(trips_id);
        if(count==2) {
            redisUtils.set("order"+order.getOrder_id(),order,7200);//存入缓存
            redisUtils.del("orderList"+order.getOrder_user_id());
        }else {
            transactionManager.rollback(status);//进行回滚
        }

        return count;
    }

    @Override
    public List<Orders> getOrderListByUser(int user_id) {
        //从缓存中取出数据
        String jsonString=(String) redisUtils.get("orderList" + user_id);
        List<Orders> list = JSON.parseArray(jsonString, Orders.class);
        if(list!=null&&list.size()!=0)
            return list;
        else {
            List<Orders> ordersList=null;
            ordersList = orderMapper.selectList(
                    new QueryWrapper<Orders>().eq("order_user_id", user_id).orderByDesc("order_create_time"));
            if(ordersList!=null&&ordersList.size()>0) {
                ordersList.forEach(order -> {
                    int trips_id=order.getOrder_trips_id();
                    Trips trips = tripsService.getOneById(trips_id);     //已逻辑删除的车次也会获取到
                    order.setOrder_train_name(trips.getTrips_train_name());     //获得列车名

                    Date date=trips.getTrips_start_time();
                    if(date.getTime()<=new Date().getTime()) {
                        order.setOrder_status(1);                               //设置已发车
                        orderMapper.updateById(order);
                        redisUtils.del("order"+order.getOrder_id());
                    }

                    order.setStart_date(date);                                  //设置发车日期

                    int line_id=trips.getTrips_line_id();
                    Line line = lineService.getById(line_id);
                    order.setStart_city(line.getLine_start_station_name());     //设置出发和达到城市
                    order.setEnd_city(line.getLine_end_station_name());

                });
                String orderList= JSON.toJSONString(ordersList);
                redisUtils.set("orderList"+user_id,orderList,3600);   //存入缓存

            }
            return ordersList;
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int refundTicket(int order_id) {

        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("退票操作！");
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);

        redisUtils.del("order"+order_id);
        Orders order = getOneById(order_id);
        redisUtils.del("orderList"+order.getOrder_user_id());
        order.setOrder_status(2);
        int count=orderMapper.updateById(order);  //更改订单的状态为退票状态

        int trips_id=order.getOrder_trips_id();
        int seat_level=order.getOrder_seat_level();
        if(seat_level==1)                       //相应坐席车票数量+1
            count+=tripsService.increase_first_seatnum(trips_id);
        else
            count+=tripsService.increase_secnd_seatnum(trips_id);
        if(count!=2)
            transactionManager.rollback(status);    //进行回滚
        return count;
    }

    @Override
    public Orders getOneById(int order_id) {
        Orders orders=(Orders)redisUtils.get("order"+order_id);
        if(orders!=null)
            return orders;
        else {
            orders=orderMapper.selectById(order_id);
            if(orders!=null)
                redisUtils.set("order"+order_id,orders);
            return orders;
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int changing_order(int order_id, int trips_id) {

        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("改签操作！");
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);

        redisUtils.del("order"+order_id);       //淘汰缓存
        Orders old_order = getOneById(order_id);
        redisUtils.del("orderList"+old_order.getOrder_user_id());
        int seat_level=old_order.getOrder_seat_level();
        old_order.setOrder_status(3);                   //原订单状态为已经改签状态
        int count =orderMapper.updateById(old_order);              //修改

        //对原车次的车票数量+1
        Integer old_trips_id = old_order.getOrder_trips_id();
        redisUtils.del("trips"+old_trips_id);
        if(seat_level==1)
            count+=tripsService.increase_first_seatnum(old_trips_id);
        else
            count+=tripsService.increase_secnd_seatnum(old_trips_id);

        //新车次车票数量-1
        Trips new_trips = tripsService.getOneById(trips_id);
        Float new_trips_price;
        if(seat_level==1) {
            new_trips_price = new_trips.getTrips_first_seat_price();
            count+=tripsService.decrease_first_seatnum(trips_id);
        } else {
            new_trips_price=new_trips.getTrips_second_seat_price();
            count+=tripsService.decrease_second_seatnum(trips_id);
        }

        Orders new_order=old_order;
        new_order.setOrder_id(null);
        new_order.setOrder_trips_id(trips_id);
        new_order.setOrder_status(0);   //  新订单状态为创建
        new_order.setOrder_create_time(null);
        new_order.setOrder_update_time(null);
        new_order.setOrder_price(new_trips_price);
        count+=saveOne(new_order);
        if(count!=5)
            transactionManager.rollback(status);        //进行回滚
        return 1;
    }
}
