package com.sky.mapper;

import com.sky.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface
UserMapper {


    /**
     * 通过openId查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User selectByOpenId(String openid);

    /**
     * 新建用户
     * @param user
     */
    void insert(User user);
}