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

    /**
     * 乐观锁实现
     * 对应编号的车次的一等座车票数量-1
     * @param trips_id 车次编号
     * @author yong
     * @date 2021/1/30 14:23
     * @return int
     */
    @Update("update trips set trips_first_seat_num=trips_first_seat_num-1 where trips_id=#{trips_id} and trips_first_seat_num-1>=0 ")
    public int decrease_first_seat(int trips_id);

    /**
     * 乐观锁实现
     * 对应编号的车次的二等座车票数量-1
     * @param trips_id 车次编号
     * @author yong
     * @date 2021/1/30 14:23
     * @return int
     */
    @Update("update trips set trips_second_seat_num=trips_second_seat_num-1 where trips_id=#{trips_id} and trips_second_seat_num-1>=0 ")
    public int decrease_second_seat(int trips_id);

    /**
     * 乐观锁实现
     * 对应编号的车次的一等座车票数量+1
     * @param trips_id 车次编号
     * @author yong
     * @date 2021/1/30 14:23
     * @return int
     */
    @Update("update trips set trips_first_seat_num=trips_first_seat_num+1 where trips_id=#{trips_id}  ")
    public int increas_first_seat(int trips_id);

    /**
     * 乐观锁实现
     * 对应编号的车次的二等座车票数量+1
     * @param trips_id 车次编号
     * @author yong
     * @date 2021/1/30 14:23
     * @return int
     */
    @Update("update trips set trips_second_seat_num=trips_second_seat_num+1 where trips_id=#{trips_id}  ")
    public int increas_second_seat(int trips_id);

    /**
     * 通过编号查找车次，无论它是否已经被逻辑删除，用于用户查询自己的订单时订单里的车次
     *
     * @param trips_id 车次编号
     * @author yong
     * @date 2021/1/30 14:21
     * @return com.ma.bookticket.pojo.Trips
     */
    @Select("select * from trips where trips_id=#{trips_id}")
    public Trips getOneByIdForOrder(int trips_id);
}