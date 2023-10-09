package com.sky.service.impl;

import com.github.pagehelper.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.BaseException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    public void save(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();
        //复制对象
        BeanUtils.copyProperties(employeeDTO, employee);
        //密码加密
        String password = DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());
        employee.setPassword(password);
        //设置状态
        employee.setStatus(StatusConstant.ENABLE);
        //创建、修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置创建、修改人
        //修改创建、修改人数据
        Long empId = BaseContext.getCurrentId();
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeMapper.insert(employee);
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {

        //创建分页查询：PageHelper分页查询插件
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //Page<>来自PageHelper分页查询插件
        Page<Employee> employeePage = employeeMapper.pageQuery(employeePageQueryDTO);
        //获取分页中的数据总数和数据
        long total = employeePage.getTotal();
        List<Employee> employeeList = employeePage.getResult();
        return new PageResult(total, employeeList);
    }

    @Override
    public void startOnStop(Integer status, long id) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();
        try {
            //int i = 1/0;
            employeeMapper.update(employee);
        } catch (Exception e) {
            throw new BaseException("员工状态修改异常...");
        }
    }

    /**
     * id查询员工信息
     *
     * @param id 员工id
     * @return Employee 查询员工信息
     */
    @Override
    public Employee selectById(long id) {
        Employee employee = employeeMapper.selectById(id);
        return employee;
    }

    /**
     * id修改员工信息
     * @param employeeDTO 员工信息
     * @return Result<Employee>
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();
        //复制对象
        BeanUtils.copyProperties(employeeDTO, employee);
        //修改时间
        employee.setUpdateTime(LocalDateTime.now());
        //获取修改人id
        Long empId = BaseContext.getCurrentId();
        //修改修改人数据
        employee.setUpdateUser(empId);

        employeeMapper.update(employee);
    }

}
