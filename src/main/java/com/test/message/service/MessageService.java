package com.test.message.service;

import com.test.message.domain.query.MessageEntity;

/**
 * 消息通知Service
 */
public interface MessageService {

    /**
     * 发送消息到消息队列
     */
    void sendMessageToQueue(MessageEntity messageEntity);


    /**
     * 异步发送消息，无消息队列
     */
    void sendMessageAsyncWithNoQueue(MessageEntity messageEntity);

    /**
     * 发送消息
     */
    void sendMessage(MessageEntity messageEntity);
}
