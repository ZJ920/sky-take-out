package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.CategoryMapper;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImp implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Override
    public Integer countByCategoryId(Long categoryId) {
        return null;
    }

    /**
     * 添加菜品
     *
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
     *
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        //分页插件
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        long total = page.getTotal();
        List<DishVO> result = page.getResult();
        return new PageResult(total, result);
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId 菜品分类id
     * @return 菜品
     */
    public List<Dish> selectByCategoryId(Long categoryId) {

        //调用动态条件分页查询菜品mapper:包含停、启售
        List<Dish> dishListTemp = dishMapper.selectByCategoryId(categoryId);
        List<Dish> dishList = new ArrayList<>();
        for (Dish dish : dishListTemp) {
            if (dish.getStatus() == 1) {
                //起售：返回
                dishList.add(dish);
            }
        }
        return dishList;
    }

    /**
     * 菜品起售停售
     * @param status
     * @param id
     * @return
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        List<Long> ids = new ArrayList<>();
        ids.add(id);

        if (status == StatusConstant.DISABLE){
            //如果是接受的是：停售请求（状态是起售）
            //1.检查是否有在售套餐
            List<Long> setmealsByDishIds = setmealDishMapper.getSetmealsByDishIds(ids);
            if (setmealsByDishIds != null && setmealsByDishIds.size() > 0){
                //存在起售套餐
                throw new SetmealEnableFailedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL_STATUS);
            }
            //2.修改状态
            //2.1 id查询菜品
            List<DishVO> dishVOS = dishMapper.selectDishesByIds(ids);
            for (DishVO dishVO : dishVOS) {
                //拷贝、修改状态
                BeanUtils.copyProperties(dishVO,dish);
                dish.setStatus(StatusConstant.DISABLE);
            }
            //2.2 修改
            dishMapper.update(dish);
        }else if (status == StatusConstant.ENABLE){
            //如果是接受的是：起售请求（状态是停售）
            //修改状态
            //1.id查询菜品
            List<DishVO> dishVOS = dishMapper.selectDishesByIds(ids);
            for (DishVO dishVO : dishVOS) {
                //拷贝、修改状态
                BeanUtils.copyProperties(dishVO,dish);
                dish.setStatus(StatusConstant.ENABLE);
            }
            //2.修改
            dishMapper.update(dish);
        }
    }

    /**
     * 删除菜品
     *
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
            if (StatusConstant.ENABLE.equals(dishVO.getStatus())) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断当前菜品是否能够删除---是否被套餐关联了？？
        List<Long> setmeals = setmealDishMapper.getSetmealsByDishIds(ids);
        if (setmeals != null && setmeals.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        try {
            //删除菜品表中的菜品数据
            dishMapper.deleteById(ids);
            //删除菜品关联的口味数据
            dishFlavorMapper.deleteByDishIds(ids);//后绪步骤实现
        } catch (Exception e) {
            throw new DeletionNotAllowedException(MessageConstant.DELETE_FAILED);
        }
    }

    /**
     * id查询菜品 DishVO
     *
     * @param id
     * @return
     */
    @Override
    public DishVO selectById(Long id) {
        DishVO dishVO = null;
        Long categoryId = null;

        //dish表查询
        List<Long> ids = new ArrayList();
        ids.add(id);

        List<DishVO> dishVOS = dishMapper.selectDishesByIds(ids);

        for (DishVO VO : dishVOS) {
            dishVO = VO;
        }
        if (dishVO != null) {

            //category表：获取categoryId菜品分类id设置categoryName分类名称
            categoryId = dishVO.getCategoryId();
            String categoryName = categoryMapper.selectByCategoryId(categoryId);
            dishVO.setCategoryName(categoryName);

            //flavors表:使用dish_id查询获取口味List
            List<DishFlavor> dishFlavor = dishFlavorMapper.selectByDishId(id);
            dishVO.setFlavors(dishFlavor);
        }

        return dishVO;
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     */
    @Override
    @Transactional
    public void updateById(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.update(dish);

        //删除口味表对应口味
        List<Long> dishIds = new ArrayList<>();
        dishIds.add(dish.getId());
        dishFlavorMapper.deleteByDishIds(dishIds);

        //重新添加口味
        if (dishDTO.getFlavors() != null && dishDTO.getFlavors().size() > 0) {
            dishFlavorMapper.insertBatch(dishDTO.getFlavors());
        }
    }

    /**
     * 根据分类id查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        //dish只有一个categoryId
        List<Dish> dishList = dishMapper.selectByCategoryId(dish.getCategoryId());

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
