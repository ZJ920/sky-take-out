package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询购物车
     * @param userId
     * @return
     */
    List<ShoppingCart> selectByUserId(Long userId);

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     * @return
     */
    void deleteShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     * @return
     * @param userId
     */
    void clean(Long userId);
}
