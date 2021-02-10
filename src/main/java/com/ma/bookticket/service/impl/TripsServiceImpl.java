package com.ma.bookticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ma.bookticket.mapper.LineMapper;
import com.ma.bookticket.mapper.OrderMapper;
import com.ma.bookticket.mapper.TripsMapper;
import com.ma.bookticket.pojo.Line;
import com.ma.bookticket.pojo.Orders;
import com.ma.bookticket.pojo.Trips;
import com.ma.bookticket.service.LineService;
import com.ma.bookticket.service.OrderService;
import com.ma.bookticket.service.TripsService;
import com.ma.bookticket.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

/**
 * 线路接口的实现类
 *
 * @author yong
 * @date 2021/1/21 17:24
 */
@Service
public class TripsServiceImpl implements TripsService {
    @Autowired
    private TripsMapper tripsMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private LineService lineService;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<Trips> getSomeTrips(int line_id, Date start_date) {
        Date end_date=new Date(start_date.getTime()+3600*24*1000);
        return tripsMapper.selectList(new QueryWrapper<Trips>()
                .eq("trips_line_id",line_id).between("trips_start_time",new Date(),end_date)
                .and(i->i.ge("trips_first_seat_num",1).or().ge("trips_second_seat_num",1))
        );
    }


    @Override
    public Trips getOneById(int trips_id) {
        Trips trips=(Trips) redisUtils.get("trips"+trips_id);
        if(trips!=null)
            return trips;
        else {
            trips=tripsMapper.selectById(trips_id);
            if(trips!=null)
                redisUtils.set("trips"+trips_id,trips,1800);
            return trips;
        }
    }

    @Override
    public int decrease_first_seatnum(int trips_id)  {
        //采用延时双删除策略来保证缓存与数据库最终一致性
        redisUtils.del("trips"+trips_id);
        if( tripsMapper.decrease_first_seat(trips_id) !=0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            redisUtils.del("trips"+trips_id);
            return 1;
        }
        return 0;
    }

    @Override
    public int decrease_second_seatnum(int trips_id)  {
        //采用延时双删除策略来保证缓存与数据库最终一致性
        redisUtils.del("trips"+trips_id);
        if( tripsMapper.decrease_second_seat(trips_id) !=0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            redisUtils.del("trips"+trips_id);
            return 1;
        }
        return 0;
    }

    @Override
    public int increase_first_seatnum(int trips_id) {
        //采用延时双删除策略来保证缓存与数据库最终一致性
        redisUtils.del("trips"+trips_id);
        if( tripsMapper.increas_first_seat(trips_id) !=0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            redisUtils.del("trips"+trips_id);
            return 1;
        }
        return 0;
    }

    @Override
    public int increase_secnd_seatnum(int trips_id) {
        //采用延时双删除策略来保证缓存与数据库最终一致性
        redisUtils.del("trips"+trips_id);
        if( tripsMapper.increas_second_seat(trips_id) !=0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            redisUtils.del("trips"+trips_id);
            return 1;
        }
        return 0;
    }

    @Override
    public List<Trips> getChangingTrips(int order_id) {
        Orders order = orderService.getOneById(order_id);
        int seat_level = order.getOrder_seat_level();       //坐席级别
        Integer trips_id = order.getOrder_trips_id();
        Trips trips = getOneById(trips_id);
        int line_id=trips.getTrips_line_id();               //线路编号
        Line line = lineService.getById(line_id);
        String start_station_name=line.getLine_start_station_name();
        String end_station_name=line.getLine_end_station_name();
        List<Trips> result;

        if(seat_level==1)
            result = tripsMapper.selectList(new QueryWrapper<Trips>().eq("trips_line_id",line_id).ne("trips_id",trips_id).
                                gt("trips_first_seat_num",0).gt("trips_start_time",new Date()));
        else
            result = tripsMapper.selectList(new QueryWrapper<Trips>().eq("trips_line_id",line_id).ne("trips_id",trips_id).
                                gt("trips_second_seat_num",0).gt("trips_start_time",new Date()));

        result.forEach(res->{
            res.setTrips_start_station_name(start_station_name);
            res.setTrips_end_station_name(end_station_name);
        });

        return result;
    }

    @Override
    public int deleteEveryDay() {
        return tripsMapper.delete(new QueryWrapper<Trips>().eq("trips_delete_flag",0).
                                                            lt("trips_start_time",new Date()));
    }
}
