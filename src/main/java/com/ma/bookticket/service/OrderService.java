package com.ma.bookticket.service;

import com.ma.bookticket.pojo.Orders;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提供列车相关操作的接口方法
 * @author yong
 * @date 2021/1/24 16:16
 */
@Service
public interface OrderService {
    /**
     *
     *
     * @param order 创建订单，同时该车次的对应的车票数量也要减一
     * @author yong
     * @date 2021/1/24 22:22
     * @return int
     */

    public int saveOne(Orders order);

    /**
     * 通过用户id得到该用户的订单列表
     * @param user_id 用户编号
     * @author yong
     * @date 2021/1/27 16:58
     * @return java.util.List<com.ma.bookticket.pojo.Orders>
     */

    public List<Orders> getOrderListByUser(int user_id);

    /**
     * 进行退票操作，并且相应车次的相应坐席的车票数量要+1
     * @param order_id 订单编号
     * @author yong
     * @date 2021/1/27 21:36
     * @return int
     */

    public int refundTicket(int order_id);

    /**
     * 根据订单编号获取订单信息
     * @param order_id 订单编号
     * @author yong
     * @date 2021/1/28 17:11
     * @return com.ma.bookticket.pojo.Orders
     */

    public Orders getOneById(int order_id);

    /**
     * 进行改签操作（在事务中）
     * 对原有的订单状态修改为已改签，对选择的车次生成相应的订单，订单大部分信息由原来的订单信息里获得
     * 原订单相应车次的相应坐席票数+1 ，新订单对应的车次的对应坐席车票数量-1
     * @param order_id 订单编号，需要改签的订单
     * @param trips_id 车次编号，选择改签的车次
     * @author yong
     * @date 2021/1/28 17:11
     * @return int
     */

    public int changing_order(int order_id,int trips_id);
}
