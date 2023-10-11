package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImp implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Override
    public Integer countByCategoryId(Long categoryId) {
        return null;
    }

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @Override
    @Transactional//开启事务
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //向菜品表插入1条数据
        dishMapper.insert(dish);//后绪步骤实现

        //获取insert语句生成的主键值
        Long dishId = dish.getId();

        //*id、dish_id、name、value:
        //  *、*、      忌口、 "[""不要葱"",""不要蒜"",""不要香菜"",""不要辣""]"
        //  *、*、      温度、 "[""热饮"",""常温"",""去冰"",""少冰"",""多冰""]"
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                //为flavors中每一条属性加上dish_id
                dishFlavor.setDishId(dishId);
            });
            //使用flavors向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);//后绪步骤实现
        }

    }

    /**
     * 动态条件分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult page(DishPageQueryDTO dishPageQueryDTO){
        //分页插件
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        long total = page.getTotal();
        List<DishVO> result = page.getResult();
        return new PageResult(total,result);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public void deleteById(List<Long> ids) {

        //批量查询
        List<DishVO> dishVOS = dishMapper.selectDishesByIds(ids);
        //判断当前菜品是否能够删除---是否存在起售中的菜品？？
        for (DishVO dishVO : dishVOS) {
            if (StatusConstant.ENABLE.equals(dishVO.getStatus())){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断当前菜品是否能够删除---是否被套餐关联了？？
        List<Long> setmeals = setmealDishMapper.getSetmealsByDishIds(ids);
        if (setmeals != null && setmeals.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        try {
            //删除菜品表中的菜品数据
            dishMapper.deleteById(ids);
            //删除菜品关联的口味数据
            dishFlavorMapper.deleteByDishId(ids);//后绪步骤实现
        } catch (Exception e) {
            throw new DeletionNotAllowedException(MessageConstant.DELETE_FAILED);
        }
    }
}
