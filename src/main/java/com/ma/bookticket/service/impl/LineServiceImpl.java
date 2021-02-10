package com.ma.bookticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ma.bookticket.mapper.LineMapper;
import com.ma.bookticket.pojo.Line;
import com.ma.bookticket.service.LineService;
import com.ma.bookticket.utils.RedisUtils;
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
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Line getOne(String station_start_name, String station_end_name) {
        //查看缓存
        Line line=(Line)redisUtils.get("line"+station_start_name+station_end_name);
        if(line!=null)
            return line;
        else {
            Map<String, Object> map = new HashMap<>();
            map.put("line_start_station_name",station_start_name);
            map.put("line_end_station_name",station_end_name);
            line=lineMapper.selectOne(new QueryWrapper<Line>().allEq(map));
            if(line!=null)
                redisUtils.set("line"+station_start_name+station_end_name,line,18000);//插入缓存中
            return line;
        }

    }
    @Override
    public Line getById(int line_id) {

        Line line=(Line) redisUtils.get("line"+line_id);
        if(line!=null)
            return line;
        else  {
            line=lineMapper.selectById(line_id);
            if (line!=null)
                redisUtils.set("line"+line_id,line,18000);
            return line;
        }
    }
}
