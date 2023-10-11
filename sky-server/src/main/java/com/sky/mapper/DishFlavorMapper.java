package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {


    /**
     * 插入一个菜品的多条口味数据
     * @param flavors 口味数据
     */
    void insertBatch(List<DishFlavor> flavors);
}
