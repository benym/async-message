package com.test.message.controller;


import com.test.message.domain.dto.UserDTO;
import com.test.message.domain.query.UserQueryParam;
import com.test.message.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testuser")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 保存用户，并发送消息通知
     *
     * @param userQueryParam 用户信息
     * @return Integer
     */
    @PostMapping("/save")
    public Integer saveUser(@Validated @RequestBody UserQueryParam userQueryParam) {
        UserDTO userDTO = new UserDTO();
        BeanCopier beanCopier = BeanCopier.create(UserQueryParam.class, UserDTO.class, false);
        beanCopier.copy(userQueryParam, userDTO, null);
        return userService.save(userDTO);
    }

    /**
     * 保存用户，并发送消息通知，异步，不使用消息队列
     *
     * @param userQueryParam 用户信息
     * @return Integer
     */
    @PostMapping("/saveAsyncNoQueue")
    public Integer saveUserAsyncWithNoQueue(@Validated @RequestBody UserQueryParam userQueryParam) {
        UserDTO userDTO = new UserDTO();
        BeanCopier beanCopier = BeanCopier.create(UserQueryParam.class, UserDTO.class, false);
        beanCopier.copy(userQueryParam, userDTO, null);
        return userService.saveAsyncWithNoQueue(userDTO);
    }

    /**
     * 保存用户，并发送消息通知，异步，使用消息队列
     *
     * @param userQueryParam 用户信息
     * @return Integer
     */
    @PostMapping("/saveWithQueue")
    public Integer saveUserWithQueue(@Validated @RequestBody UserQueryParam userQueryParam) {
        UserDTO userDTO = new UserDTO();
        BeanCopier beanCopier = BeanCopier.create(UserQueryParam.class, UserDTO.class, false);
        beanCopier.copy(userQueryParam, userDTO, null);
        return userService.saveWithQueue(userDTO);
    }
}
