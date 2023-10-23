package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 购物车Mapper
 */
@Mapper
public interface ShoppingCartMapper {

    /**
     *动态条件查询购物车表：通过【菜品或套餐】（2选1必须）、【口味】（非必须）
     * @param shoppingCart 购物车
     * @return
     */
    List<ShoppingCart> findById(ShoppingCart shoppingCart);

    /**
     * 更新购物车数据
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入购物车数据
     *
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            " values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 查询购物车
     * @param userId
     * @return
     */
    @Select("select * from shopping_cart where user_id = #{currentId}")
    List<ShoppingCart> findByUserId(Long userId);

    /**
     * 删除购物车中商品
     * @param shoppingCart
     * @return
     */
    void delete(ShoppingCart shoppingCart);
}
