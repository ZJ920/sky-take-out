package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate<String, DishVO> redisTemplate;


    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {

        //使用redis缓存菜品分类
        //向redis查询
        //1.拼接key：dish_分类id
        String key = "dish_"+categoryId;//例：dish_7
        //List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        //------------------------------------------------------
        List<DishVO> list = redisTemplate.opsForList().range(key, 0, -1);
        //------------------------------------------------------

        //如果redis有缓存，则直接返回缓存
        if (list != null &&  list.size() > 0){
            return Result.success(list);
        }

        //如果redis中没有缓存，则查询数据库
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//设置查询菜品条件：起售

        list = dishService.listWithFlavor(dish);
        //将数据存入redis
        //redisTemplate.opsForValue().set(key,list);
        //------------------------------------------------------
        for (DishVO dishVO : list) {
            redisTemplate.opsForList().rightPush(key,dishVO);
        }
        //------------------------------------------------------
        return Result.success(list);
    }

}
