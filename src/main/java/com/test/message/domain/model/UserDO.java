package com.test.message.domain.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * User表实体类
 */
@TableName("user")
@Data
public class UserDO implements Serializable {

    private static final long serialVersionUID = -5268838066640437272L;
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户手机号
     */
    @TableField("user_phone")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @TableField("user_email")
    private String userEmail;
}