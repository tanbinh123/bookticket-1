package com.ma.bookticket.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 为字段自动填充值(orders表)
 * @author yong
 * @date 2021/1/24 16:43
 */
@Component
public class handler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "order_create_time", Date.class,new Date());
        //this.strictInsertFill(metaObject, "order_update_time", Date.class,new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"order_update_time", Date.class,new Date());
    }
}
