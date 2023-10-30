package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.vo.EmployeeEditPasswordVO;

import java.util.List;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void startOnStop(Integer status, long id);

    /**
     * id查询员工信息
     *
     * @param id 员工id
     * @return Employee 查询员工信息
     */
    Employee selectById(long id);

    /**
     * id修改员工信息
     * @param employeeDTO 员工信息
     * @return Result<Employee>
     */
    void update(EmployeeDTO employeeDTO);

    /**
     *员工修改密码
     * @return
     */
    void editPassword(EmployeeEditPasswordVO employeeEditPasswordVO);
}