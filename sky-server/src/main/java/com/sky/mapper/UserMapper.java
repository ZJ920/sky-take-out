package com.sky.mapper;

import com.sky.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface
UserMapper {


    /**
     * 通过openId查询用户
     *
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User selectByOpenId(String openid);

    /**
     * 新建用户
     *
     * @param user
     */
    void insert(User user);

    /**
     * 通过主键（token实现）查询用户
     *
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId}")
    User selectById(Long userId);

    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
