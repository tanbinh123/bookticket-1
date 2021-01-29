package com.ma.bookticket.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName station
 */
@Data
@TableName(value = "station")
public class Station implements Serializable {
    /**
     * 编号
     *
     * @mbg.generated Sun Jan 17 23:52:43 CST 2021
     */
    @TableId(type = IdType.AUTO)
    private Integer station_id;

    /**
     * 站名
     *
     * @mbg.generated Sun Jan 17 23:52:43 CST 2021
     */
    private String station_name;

    /**
     * 地址
     *
     * @mbg.generated Sun Jan 17 23:52:43 CST 2021
     */
    private String station_adress;

}