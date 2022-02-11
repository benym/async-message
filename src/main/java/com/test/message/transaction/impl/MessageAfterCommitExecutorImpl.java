package com.test.message.transaction.impl;


import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.test.message.transaction.MessageAfterCommitExecutor;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class MessageAfterCommitExecutorImpl implements TransactionSynchronization,
        MessageAfterCommitExecutor {

    private static final Logger logger = LoggerFactory.getLogger(MessageAfterCommitExecutorImpl.class);

    private static final ThreadLocal<List<Runnable>> RUNNABLES = new ThreadLocal<>();

    @Autowired
    private ListeningExecutorService listeningExecutorService;

    /**
     * 当该方法被调用时，会检查当前线程的同步器是否处于激活状态，即上下文是否存在事务
     * <p/>
     * 如果没有，则立即执行runnable
     * <p/>
     * 否则，将提交的runnable存储在一个ThreadLocal变量中
     * <p/>
     * 如果这是当前线程第一次提交runnable
     * <p/>
     * 那么我们会将自身注册为当前进程的事务同步器(如果同步没有激活，则无法注册)
     * <p/>
     *
     * @param runnable 由外部提交的线程
     */
    @Override
    public void execute(Runnable runnable) {
        logger.info("【事务已提交】新线程开始运行:{}", runnable);
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            logger.info("【当前方法无事务】立即执行消息入库操作:{}", runnable);
            runnable.run();
            return;
        }
        List<Runnable> threadRunnables = RUNNABLES.get();
        if (threadRunnables == null) {
            threadRunnables = new ArrayList<>();
            RUNNABLES.set(threadRunnables);
            TransactionSynchronizationManager.registerSynchronization(this);
        }
        threadRunnables.add(runnable);
    }

    /**
     * 因为注册了事务同步器，所以只要事务成功提交，就会调用afterCommit()方法
     * <p/>
     * 此时，我们为成功完成事务的线程获取所有提交的runnable对象，并采用异步线程池执行他们
     * <p/>
     */
    @Override
    public void afterCommit() {
        List<Runnable> threadRunnables = RUNNABLES.get();
        logger.info("【事务提交成功】开始执行线程:{}", threadRunnables);
        threadRunnables.forEach(nowRunnable -> {
            logger.info("【执行线程】:{}", nowRunnable);
            try {
                listeningExecutorService.submit(nowRunnable);
            } catch (Exception e) {
                logger.error("【事务线程】执行失败", e);
            }
        });
    }

    /**
     * 为刚刚完成事务的线程清理ThreadLocal变量
     *
     * @param status 当前事务状态
     */
    @Override
    public void afterCompletion(int status) {
        logger.info("【事务已完成】状态为:{}", status == STATUS_COMMITTED ? "已完成" : "已回滚");
        RUNNABLES.remove();
    }
}
