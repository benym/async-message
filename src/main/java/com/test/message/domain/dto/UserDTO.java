package com.test.message.domain.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * userDTO
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -7055429600592391854L;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户邮箱
     */
    private String userEmail;
}
