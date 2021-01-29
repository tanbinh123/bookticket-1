package com.ma.bookticket.service.impl;

import com.ma.bookticket.mapper.TrainMapper;
import com.ma.bookticket.pojo.Train;
import com.ma.bookticket.service.TrainService;
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
    TrainMapper trainMapper;
    @Override
    public Train getOneById(int train_id) {
        return trainMapper.selectById(train_id);
    }
}
