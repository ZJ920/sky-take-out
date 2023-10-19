package com.sky.service.impl;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    /**
     * 微信用户登录
     * @param userLoginDTO code
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {

        return null;
    }
}
