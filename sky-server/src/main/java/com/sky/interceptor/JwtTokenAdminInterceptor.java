package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.exception.AdminException;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *拦截器之前执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            ///admin/employee/login 是
            ///admin/employee/ 不是
            //使用反射机制获取路径上的方法名称
            //当前拦截到的不是动态方法，直接放行
            log.info("放行静态资源...");
            return true;
        }

        //1、从请求头中获取令牌
        //jwtProperties.getAdminTokenName() = admin-token-name: token(yml配置文件中)
        String token = request.getHeader(jwtProperties.getAdminTokenName());
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            //验证token，如果验证token失败则会抛出异常
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("当前员工id：{}", empId);
            //它提供了线程局部变量的功能。每个 ThreadLocal 实例都维护了一个独立的变量副本，每个线程可以访问到自己的变量副本而不会受其他线程的影响。
            BaseContext.setCurrentId(empId);
            log.info("jwt校验通过");

            //校验员工权限
            if ("/admin/employee/page".equals(requestURI) && "GET".equals(method)){
                if (empId != 1){
                    throw new AdminException();
                }
            }
            if ("/admin/employee".equals(requestURI) && "POST".equals(method)){
                if (empId != 1){
                    throw new AdminException();
                }
            }
            //3、通过，放行
            return true;
        } catch (SignatureException s){
            //4、不通过，响应401状态码
            log.info("jwt校验不通过");
            //前后端分离页面跳转由前端实现：前端根据响应的代码判断
            response.setStatus(401);
            return false;
        } catch (AdminException r){
            log.info("员工没有权限");
            throw new AdminException("没有管理员权限");
        }
    }
}