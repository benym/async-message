package com.test.message.listener;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.test.message.domain.query.MessageEntity;
import com.test.message.service.MessageService;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 消息队列监听器-消费者, spring上下文准备就绪时，监听队列内消息进行消费
 */
@Component
public class MessageQueueListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(MessageQueueListener.class);

    // 限制队列长度
    private static final Integer MAX_MESSAGEQUEUE_SIZE = 1000;

    // 阻塞队列，并发安全
    public static final LinkedBlockingQueue<MessageEntity> messageQueue = new LinkedBlockingQueue<>(
            MAX_MESSAGEQUEUE_SIZE);

    @Autowired
    private ListeningExecutorService listeningExecutorService;

    @Resource
    private MessageService messageService;

    /**
     * 消息消费者
     * <p/>
     * 如果循环在异步提交外围
     * <p/>
     * 可能出现消息还没发送完，while循环又判断了
     * <p/>
     * 多次提交了任务，但此时队列已经为空
     * <p/>
     * 所以这里while循环在线程池提交的任务内部
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        listeningExecutorService.submit(() -> {
            try {
                while (true) {
                    if (messageQueue.size() != 0) {
                        // take 队列为空的时候线程阻塞不会执行后续方法
                        MessageEntity message = messageQueue.take();
                        messageService.sendMessage(message);
                    }
                }
            } catch (Exception e) {
                logger.error("队列: 消息发送失败", e);
            }
        });
    }
}
