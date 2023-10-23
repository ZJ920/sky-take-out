package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //只能查询自己的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());//用户id

        //判断购物车表有没有相同【菜品或套餐】、【口味】
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.findById(shoppingCart);
        if (shoppingCartList != null && shoppingCartList.size() > 0){
            //购物车表存在相同【菜品或套餐】、【口味】
            shoppingCart = shoppingCartList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber()+1);
            //更新购物车
            shoppingCartMapper.updateNumberById(shoppingCart);
        }else {
            //设置表中的属性
            if (shoppingCart.getDishId() != null){
                //菜品：name、image、amount、| shoppingCart中存在dish_id、dish_flavor
                Dish dish = dishMapper.selectDishesById(shoppingCart.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());//价格
            }else {
                //套餐:name、image、amount、| shoppingCart中存在setmealId
                Setmeal setmeal = setmealMapper.getById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            //通用属性
            shoppingCart.setNumber(1);//添加数量：1
            shoppingCart.setCreateTime(LocalDateTime.now());//创建时间
            //插入数据
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> selectByUserId(Long userId) {
        return shoppingCartMapper.findByUserId(userId);
    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     * @return
     */
    @Override
    public void deleteShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //只能查询自己的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());//用户id

        //判断商品数量
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.findById(shoppingCart);
        shoppingCart = shoppingCartList.get(0);
        if (shoppingCart.getNumber() > 1){
            //商品数量大于1，数量减1
            shoppingCart.setNumber(shoppingCart.getNumber() - 1);
            shoppingCartMapper.updateNumberById(shoppingCart);
        }else {
            //删除数据
            shoppingCartMapper.delete(shoppingCart);
        }
    }

    /**
     * 清空购物车
     * @return
     */
    @Override
    public void clean(Long userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCartMapper.delete(shoppingCart);
    }
}
