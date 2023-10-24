package com.sky.mapper;


import com.sky.SkyApplication;
import com.sky.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = SkyApplication.class)
@RunWith(SpringRunner.class)
@MapperScan("com.sky.mapper")
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectByIdUserId(){
        User user = userMapper.selectById(5L);
        System.out.println(user);
    }
}
