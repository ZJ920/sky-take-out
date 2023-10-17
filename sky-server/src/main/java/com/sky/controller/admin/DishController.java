package com.sky.controller.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("添加菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("添加菜品: {}" , dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("动态条件分页查询菜品")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("动态条件分页查询菜品: {}" , dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("id查询菜品")
    public Result<DishVO> selectById(@PathVariable Long id){
        log.info("id查询菜品: {}" , id);
        DishVO dishVO = dishService.selectById(id);
        return Result.success(dishVO);
    }

    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteById(@RequestParam List<Long> ids){
        log.info("批量删除菜品:{}" , ids);
        dishService.deleteById(ids);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateById(@RequestBody DishDTO dishDTO){
        log.info("修改菜品:{}" , dishDTO);
        dishService.updateById(dishDTO);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品(过滤停售)
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        log.info("根据分类id查询菜品:{}" , categoryId);
        List<Dish> dishList = dishService.selectByCategoryId(categoryId);
        return Result.success(dishList);
    }

    /**
     * 菜品起售停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        dishService.startOrStop(status, id);
        return Result.success();
    }
}
