package com.ma.bookticket.service;

import com.ma.bookticket.pojo.Train;
import org.springframework.stereotype.Service;

/**
 * 提供列车相关操作的接口方法
 *
 * @author yong
 * @date 2021/1/21 15:38
 */

@Service
public interface TrainService {
    /**
     *
     * 通过列车id得到列车对象
     * @author yong
     * @date 2021/1/21 15:40
     * @return java.lang.String
     */

    public Train getOneById(int train_id);

}
