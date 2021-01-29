package com.ma.bookticket.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName admin
 */
@Data
@TableName(value = "admin")
public class Admin implements Serializable {
    /**
     * 编号
     *
     * @mbg.generated Sun Jan 17 23:54:31 CST 2021
     */
    @TableId(type = IdType.AUTO)
    private Integer admin_id;

    /**
     * 登陆名
     *
     * @mbg.generated Sun Jan 17 23:54:31 CST 2021
     */
    private String admin_login_name;

    /**
     * 密码
     *
     * @mbg.generated Sun Jan 17 23:54:31 CST 2021
     */
    private String admin_password;

}