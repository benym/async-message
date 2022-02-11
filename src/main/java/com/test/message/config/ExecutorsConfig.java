package com.test.message.config;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将线程池交给spring管理
 */
@Configuration
public class ExecutorsConfig {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorsConfig.class);

    public static final int MAX_POOL_SIZE = 100;

    @Bean
    public ListeningExecutorService injectExecutorService() {
        int coreThreadNum = Runtime.getRuntime().availableProcessors();
        int maxThreadNum = coreThreadNum * 2;
        logger.info("初始化线程池，核心线程数:{},最大线程数:{}", coreThreadNum, maxThreadNum);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("自定义线程名称-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(coreThreadNum, maxThreadNum,0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(MAX_POOL_SIZE), threadFactory, new AbortPolicy());
        return MoreExecutors.listeningDecorator(executorService);
    }

}
