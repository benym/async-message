package com.test.message.service;


import com.test.message.domain.dto.UserDTO;

public interface UserService {

    /**
     * 保存用户并发送通知，串行
     *
     * @param userDTO 用户实体
     * @return Integer
     */
    Integer save(UserDTO userDTO);

    /**
     * 保存用户并发送异步通知，未使用消息队列
     *
     * @param userDTO 用户实体
     * @return Integer
     */
    Integer saveAsyncWithNoQueue(UserDTO userDTO);

    /**
     * 保存用户并发送异步通知，使用消息队列
     *
     * @param userDTO 用户实体
     * @return Integer
     */
    Integer saveWithQueue(UserDTO userDTO);
}
