package com.sky.controller.user;


import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C端-购物车接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result<String> add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        //dishId、setmealId、dishFlavor;
        log.info("添加购物车：{}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);//后绪步骤实现
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list(){
        log.info("查看购物车");
        List<ShoppingCart> shoppingCartList = shoppingCartService.selectByUserId(BaseContext.getCurrentId());
        return Result.success(shoppingCartList);
    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    @ApiOperation("删除购物车中一个商品")
    public Result<String> delete(@RequestBody ShoppingCartDTO shoppingCartDTO){
        //dishId、setmealId、dishFlavor;
        log.info("删除购物车中一个商品：{}", shoppingCartDTO);
        shoppingCartService.deleteShoppingCart(shoppingCartDTO);//后绪步骤实现
        return Result.success();
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        log.info("清空购物车");
        shoppingCartService.clean(BaseContext.getCurrentId());
        return Result.success();
    }
}