package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeEditPasswordVO;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        //public static final String EMP_ID = "empId";
        //EMP_ID：empId（固定值）

        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),//密钥名称：admin-secret-key: itcast（配置文件）、String
                jwtProperties.getAdminTtl(),//密钥过期时间：admin-ttl: 7200000（2h、配置文件）、long
                //claims:在载荷部分，我们可以定义自己所需的声明来存储一些额外的数据。通过将员工的ID放入JWT的声明中，可以在验证和解析JWT时方便地获取到员工的ID信息。
                //claims：包含要添加到JWT负载（Payload）的声明信息的Map对象。可以在此参数中添加自定义的声明，也可以包含一些标准的声明如 ("iss"（签发者）、"sub"（主题）、"exp"（过期时间）)。
                claims//键值对
        );
        //使用@Buider注解快速创建employeeLoginVO对象
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出登录")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @return
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工:{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询员工
     *
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("员工分页（条件）查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页（条件）查询:{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 员工状态更新
     *
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("员工状态更新")
    //http://localhost:8080/admin/employee/status/1?id=7
    //{status}:@PathVariable:url路径获取参数
    //?id=7：地址栏获取参数
    public Result startOnStop(@PathVariable Integer status, long id) {
        log.info("员工状态更新:启用状态：{},用户id：{}", status, id);
        employeeService.startOnStop(status, id);
        return Result.success();
    }

    /**
     * id查询员工信息
     *
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("id查询员工信息")
    public Result<Employee> selectById(@PathVariable long id) {
        log.info("id查询员工信息:{}", id);
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    /**
     * id修改员工信息
     *
     * @param employeeDTO 员工信息
     * @return Result<Employee>
     */
    @PutMapping
    @ApiOperation("id修改员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("id修改员工信息:{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * 员工修改密码
     *
     * @return
     */
    @PutMapping("/editPassword")
    @ApiOperation("员工修改密码")
    public Result editPassword(@RequestBody EmployeeEditPasswordVO employeeEditPasswordVO) {
        log.info("员工修改密码：{}", employeeEditPasswordVO);
        employeeService.editPassword(employeeEditPasswordVO);
        return Result.success();
    }
}
