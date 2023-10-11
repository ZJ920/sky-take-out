package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDishVO {
    private int id;
    private int setmealId;
    private String dishId;
    private String name;
    private String price;
    private int copies;
}
