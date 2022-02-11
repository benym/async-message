package com.test.message.domain.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserQueryParam implements Serializable {

    private static final long serialVersionUID = 7076273323311739844L;
    /**
     * 用户名
     */
    @NotNull
    @JsonProperty("UserName")
    private String userName;

    /**
     * 用户手机号
     */
    @NotNull
    @JsonProperty("UserPhone")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @NotNull
    @JsonProperty("UserEmail")
    private String userEmail;
}
