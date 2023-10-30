package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "员工修改密码")
public class EmployeeEditPasswordVO implements Serializable {

    @ApiModelProperty("员工id")
    Long empId;

    @ApiModelProperty("新密码")
    String newPassword;

    @ApiModelProperty("旧密码")
    String oldPassword;

}
