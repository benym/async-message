package com.test.message.service.impl;

import static com.test.message.listener.MessageQueueListener.messageQueue;

import com.test.message.domain.query.MessageEntity;
import com.test.message.service.MessageService;
import com.test.message.transaction.MessageAfterCommitExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageAfterCommitExecutor messageAfterCommitExecutor;

    /**
     * 消息生产者
     * <p/>
     * 注意由事务管理器提交的runnable进程，如果涉及到写入操作
     * <p/>
     * 则必须采用Propagation.REQUIRES_NEW的事务传播类型
     * <p/>
     * 现有管理器是上文事务提交之后执行新线程
     * <p/>
     * 如果新线程内未开启事务，则会因传播机制加入到上文事务中
     * <p/>
     * 由于上文事务已提交，所以新线程的执行在事务完成之后
     * <p/>
     * 将会出现线程虽然执行了，但事务未提交的情况，导致写操作失败
     * <p/>
     * 本次生产者仅将消息实体加入到消息队列，无需额外声明事务传播类型
     */
    @Override
    public void sendMessageToQueue(MessageEntity messageEntity) {
        messageAfterCommitExecutor.execute(() -> {
            messageQueue.offer(messageEntity);
        });
    }

    /**
     * 异步发送消息，未使用消息队列
     */
    @Override
    public void sendMessageAsyncWithNoQueue(MessageEntity messageEntity) {
        messageAfterCommitExecutor.execute(() -> {
            sendMessage(messageEntity);
        });
    }

    /**
     * 发送消息具体业务逻辑
     */
    @Override
    public void sendMessage(MessageEntity messageEntity) {
        messageEntity.getReceiver().forEach(receiver -> {
            logger.info("发送消息，消息发送者:{}，消息接受者:{}", messageEntity.getSender(), receiver);
        });
    }
}
