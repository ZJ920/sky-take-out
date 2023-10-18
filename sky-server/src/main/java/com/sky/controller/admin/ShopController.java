package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * 营业状态相关
 */
@RestController("adminShopController")//防止与user的ShopController（@Component）在创建到spring容器里时Bean名称冲突
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "营业状态相关")
public class ShopController {
    //优雅
    private static final String KEY = "SHOP_STATUS";
    private static final Integer INIT_STATUS = 0;

    //这里使用redis代替数据库保存status
    @Autowired
    RedisTemplate<String,Integer> redisTemplate;

    /**
     * 设置营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result setStatus(@PathVariable Integer status){
        //店铺营业状态：1为营业，0为打烊
        log.info("设置营业状态为： {}", status);
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }

    /**
     * 查询营业状态
     * @return Result<Integer>营业状态
     */
    @GetMapping("/status")
    @ApiOperation("查询营业状态")
    public Result<Integer> selectStatus(){
        if (redisTemplate.opsForValue().get(KEY) == null){
            //如果redis中没有营业状态，则先设置默认营业状态为0
            redisTemplate.opsForValue().set(KEY,INIT_STATUS);
        }
        Integer status = redisTemplate.opsForValue().get(KEY);
        return Result.success(status);
    }
}
