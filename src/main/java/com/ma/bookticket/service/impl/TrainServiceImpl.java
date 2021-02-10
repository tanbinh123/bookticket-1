package com.ma.bookticket.service.impl;

import com.ma.bookticket.mapper.TrainMapper;
import com.ma.bookticket.pojo.Train;
import com.ma.bookticket.service.TrainService;
import com.ma.bookticket.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 列车服务类的实现类
 * @author yong
 * @date 2021/1/21 15:41
 */
@Service
public class TrainServiceImpl implements TrainService {
    @Autowired
    private TrainMapper trainMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Train getOneById(int train_id) {
        Train train=(Train) redisUtils.get("train"+train_id);       //查找缓存
        if(train!=null)
            return train;
        else {
            train=trainMapper.selectById(train_id);
            if(train!=null)
                redisUtils.set("train"+train_id,train,18000);        //存入缓存
            return train;
        }
    }
}
