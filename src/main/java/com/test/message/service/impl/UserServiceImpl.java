package com.test.message.service.impl;


import com.test.message.domain.dto.UserDTO;
import com.test.message.domain.model.UserDO;
import com.test.message.domain.query.MessageEntity;
import com.test.message.mapper.UserMapper;
import com.test.message.service.MessageService;
import com.test.message.service.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.Resource;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MessageService messageService;

    @Transactional
    @Override
    public Integer save(UserDTO userDTO) {
        UserDO userDO = new UserDO();
        BeanCopier beanCopier = BeanCopier.create(UserDTO.class, UserDO.class, false);
        beanCopier.copy(userDTO, userDO, null);
        Integer result = userMapper.insert(userDO);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSender(userDO.getUserName());
        messageEntity.setContent("成功创建用户");
        List<String> recevier = generalData(10000);
        messageEntity.setReceiver(recevier);
        // 直接调用消息发送模块
        messageService.sendMessage(messageEntity);

        // 基本异步方案
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                TransactionSynchronizationManager.registerSynchronization(
//                        new TransactionSynchronizationAdapter() {
//                            @Override
//                            public void afterCommit() {
//                                messageService.sendMessage(messageEntity);
//                            }
//                        });
//            }
//        });
//        thread.start();
        return result;
    }

    @Override
    public Integer saveAsyncWithNoQueue(UserDTO userDTO) {
        UserDO userDO = new UserDO();
        BeanCopier beanCopier = BeanCopier.create(UserDTO.class, UserDO.class, false);
        beanCopier.copy(userDTO, userDO, null);
        Integer result = userMapper.insert(userDO);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSender(userDO.getUserName());
        messageEntity.setContent("成功创建用户");
        List<String> recevier = generalData(10000);
        messageEntity.setReceiver(recevier);
        // 直接异步发送消息
        messageService.sendMessageAsyncWithNoQueue(messageEntity);
        return result;
    }

    @Transactional
    @Override
    public Integer saveWithQueue(UserDTO userDTO) {
        UserDO userDO = new UserDO();
        BeanCopier beanCopier = BeanCopier.create(UserDTO.class, UserDO.class, false);
        beanCopier.copy(userDTO, userDO, null);
        Integer result = userMapper.insert(userDO);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSender(userDO.getUserName());
        messageEntity.setContent("成功创建用户");
        List<String> recevier = generalData(10000);
        messageEntity.setReceiver(recevier);
        // 将消息发送给队列
        messageService.sendMessageToQueue(messageEntity);
        return result;
    }

    public List<String> generalData(Integer num) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            data.add("朋友" + i);
        }
        return data;
    }
}
