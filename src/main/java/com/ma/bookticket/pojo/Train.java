package com.ma.bookticket.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName train
 */
@Data
@TableName(value = "train")
public class Train implements Serializable {
    /**
     * 编号
     *
     * @mbg.generated Sun Jan 17 23:51:46 CST 2021
     */
    @TableId(type = IdType.AUTO)
    private Integer train_id;

    /**
     * 列车名称，类似G6340
     *
     * @mbg.generated Sun Jan 17 23:51:46 CST 2021
     */
    private String train_name;

    /**
     * 列车的最大速度
     *
     * @mbg.generated Sun Jan 17 23:51:46 CST 2021
     */
    private Integer train_speed;

    /**
     * 列车的座位数量
     *
     * @mbg.generated Sun Jan 17 23:51:46 CST 2021
     */
    private Integer train_seat_num;

}