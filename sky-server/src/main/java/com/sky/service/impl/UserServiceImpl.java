package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.BaseException;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 微信用户登录
     *
     * @param userLoginDTO code
     * @return
     */
    @Override
   public User login(UserLoginDTO userLoginDTO) {
        //获取登录时传入的code
        String code = userLoginDTO.getCode();
        //根据code获取openid
        String openid = getOpenId(code);
        if (openid == null){
            //登录失败
            throw new LoginFailedException("登录失败");
        }
        //根据openid查询用户
        User user = userMapper.selectByOpenId(openid);

        if (user == null){
            //新用户，自动注册
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
            log.info("新用户自动注册成功: {}", user);
        }
        return user;
    }

    private String getOpenId(String code) {
        //创建一个map集合用于存储参数
        Map<String, String> map = new HashMap<>();
        //调用微信接口获得当前微信用户的openid
        //将appid、secret、js_code、grant_type放入map集合中
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        //调用HttpClientUtil的doGet方法，传入WX_LOGIN和map参数，获取返回的json字符串
        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        //将json字符串转换成JSONObject对象
        JSONObject jsonObject = JSON.parseObject(json);
        //从JSONObject对象中获取openid
        //如果不存在”openid“则会返回null
        return jsonObject.getString("openid");
    }
}
