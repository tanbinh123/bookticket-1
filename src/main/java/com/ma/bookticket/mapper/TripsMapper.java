package com.ma.bookticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ma.bookticket.pojo.Trips;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 *
 * 继承mybatis-plus的BaseMapper<T>,里面有通用的数据库操作方法可使用
 * 也可自定义操作trips表的接口方法，这里采用Mybatis的注解形式
 * @author yong
 * @date 2021/1/18 0:21
 */

@Mapper
public interface TripsMapper extends BaseMapper<Trips> {
    @Select("select * from trips")
    public List<Trips> selectAll();

    @Update("update trips set trips_first_seat_num=trips_first_seat_num-1 where trips_id=#{trips_id} and trips_first_seat_num-1>0 ")
    public int decrease_first_seat(int trips_id);

    @Update("update trips set trips_second_seat_num=trips_second_seat_num-1 where trips_id=#{trips_id} and trips_second_seat_num-1>0 ")
    public int decrease_second_seat(int trips_id);

    @Update("update trips set trips_first_seat_num=trips_first_seat_num+1 where trips_id=#{trips_id}  ")
    public int increas_first_seat(int trips_id);

    @Update("update trips set trips_second_seat_num=trips_second_seat_num+1 where trips_id=#{trips_id}  ")
    public int increas_second_seat(int trips_id);
}