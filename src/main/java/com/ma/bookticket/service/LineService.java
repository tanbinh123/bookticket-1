package com.ma.bookticket.service;

import com.ma.bookticket.pojo.Line;
import org.springframework.stereotype.Service;

/**
 * 提供线路操作相关接口方法
 * @author yong
 * @date 2021/1/21 15:48
 */
@Service
public interface LineService {

    /**
     * 通过起始站点和到达站点找到对应的线路信息
     *
     * @param station_start_name 起始站点名
     * @param station_end_name 到达站点名
     * @author yong
     * @date 2021/1/21 17:22
     * @return com.ma.bookticket.pojo.Line
     */

    public Line getOne(String station_start_name,String station_end_name);
    /**
     * 通过id号查找
     * @param line_id 线路编号
     * @author yong
     * @date 2021/1/22 1:08
     * @return com.ma.bookticket.pojo.Line
     */

    public Line getById(int line_id);
}
