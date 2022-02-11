package com.test.message.domain.query;

import java.util.List;
import lombok.Data;

/**
 * 消息实体类
 */
@Data
public class MessageEntity {

    /**
     * 消息发送者
     */
    private String sender;

    /**
     * 消息接受者
     */
    private List<String> receiver;

    /**
     * 消息内容
     */
    private String content;

}
