package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    Integer countByCategoryId(Long categoryId);

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 动态条件分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 动态条件分页查询菜品
     * @param ids 菜品id数组
     */
    void deleteById(List<Long> ids);

    /**
     * id查询菜品
     * @param id
     * @return
     */
    DishVO selectById(Long id);

    /**
     * 修改菜品
     * @param dishDTO
     */
    void updateById(DishDTO dishDTO);

    List<Dish> selectByCategoryId(Long categoryId);

    /**
     * 菜品起售停售
     * @param status
     * @param id
     * @return
     */
    void startOrStop(Integer status, Long id);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
