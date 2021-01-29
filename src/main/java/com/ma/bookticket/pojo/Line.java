package com.ma.bookticket.pojo;

import java.io.Serializable;
import java.sql.Time;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName line
 */
@Data
@TableName(value = "line")
public class Line implements Serializable {
    /**
     * 编号
     *
     * @mbg.generated Sun Jan 17 23:53:25 CST 2021
     */
    @TableId(type = IdType.AUTO)
    private Integer line_id;

    /**
     * 起始站点
     *
     * @mbg.generated Sun Jan 17 23:53:25 CST 2021
     */
    private String line_start_station_name;

    /**
     * 到达站点
     *
     * @mbg.generated Sun Jan 17 23:53:25 CST 2021
     */
    private String line_end_station_name;

    /**
     * 线路用时
     *
     * @mbg.generated Sun Jan 17 23:53:25 CST 2021
     */
    private Time line_time;

}