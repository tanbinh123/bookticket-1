package com.ma.bookticket.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @TableName order
 */
@Data
@TableName(value = "orders")
public class Orders implements Serializable {
    /**
     * 编号
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    @TableId(type = IdType.AUTO)
    private Integer order_id;

    /**
     * 用户编号
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    private Integer order_user_id;

    /**
     * 车次编号
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    private Integer order_trips_id;

    /**
     * 创建时间
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    @TableField(fill = FieldFill.INSERT)
    private Date order_create_time;

    /**
     * 修改时间
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date order_update_time;

    /**
     * 订单状态（订单状态（0：创建，1：已发车，2：退票，3：改签））
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    private Integer order_status;

    /**
     * 乘车人姓名
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    private String order_passenger_name;

    /**
     * 联系人姓名
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    private String order_linkman_name;

    /**
     * 联系人手机号
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    private String order_linkman_phone;

    /**
     * 乘车人身份证号码
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    private String order_passenger_identity_num;

    /**
     * 订购的坐席（一等座/二等座,分别为1，2）
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    private Integer order_seat_level;
    /**
     * 订单金额
     *
     * @mbg.generated Sun Jan 24 00:23:40 CST 2021
     */
    private Double order_price;

    //非数据库字段
    @TableField(exist = false)
    private String order_train_name;       //列车名称

    @TableField(exist = false)
    private Date start_date;                //发车日期

    @TableField(exist = false)
    private String start_city;              //出发城市

    @TableField(exist = false)
    private String end_city;                //到达城市
}