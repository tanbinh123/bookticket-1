package com.ma.bookticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ma.bookticket.mapper.LineMapper;
import com.ma.bookticket.pojo.Line;
import com.ma.bookticket.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author yong
 * @date 2021/1/21 16:15
 */
@Service
public class LineServiceImpl implements LineService {
    @Autowired
    private LineMapper lineMapper;

    @Override
    public Line getOne(String station_start_name, String station_end_name) {
        Map<String, Object> map = new HashMap<>();
        map.put("line_start_station_name",station_start_name);
        map.put("line_end_station_name",station_end_name);
        return lineMapper.selectOne(new QueryWrapper<Line>().allEq(map));
    }
    @Override
    public Line getById(int line_id) {
        return lineMapper.selectById(line_id);
    }
}
