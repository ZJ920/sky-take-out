package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.vo.UserLoginVO;

public interface UserService {

    /**
     * 微信用户登录
     * @param userLoginDTO code
     * @return
     */
    User login(UserLoginDTO userLoginDTO);
}
