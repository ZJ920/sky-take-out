package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * id查询员工信息
     *
     * @param id 员工id
     * @return Employee 查询员工信息
     */
    @Select("select * from employee where id = #{id}")
    Employee selectById(long id);

    /**
     * 新增员工
     * @param employee
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values"+
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status}, #{createTime},#{updateTime},#{createUser}, #{updateUser})")
    void insert(Employee employee);

    /**
     * 动态条件更新
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 通过empId获取员工信息
     * @param empId
     * @return
     */
    @Select("select * from employee where id = #{empId}")
    Employee getByUserId(Long empId);
}
