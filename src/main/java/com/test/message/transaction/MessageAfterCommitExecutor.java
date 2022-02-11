package com.test.message.transaction;

import java.util.concurrent.Executor;

/**
 * 继承Exector方法，用于事务提交后execute异步执行任务
 */
public interface MessageAfterCommitExecutor extends Executor {

}
