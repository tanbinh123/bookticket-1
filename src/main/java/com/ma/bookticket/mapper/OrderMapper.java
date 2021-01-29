package com.ma.bookticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ma.bookticket.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * 继承mybatis-plus的BaseMapper<T>,里面有通用的数据库操作方法可使用
 * 也可自定义操作order表的接口方法，这里采用Mybatis的注解形式
 * @author yong
 * @date 2021/1/18 0:21
 */

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}