package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class AutoFillAspect {
    //定义切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        //joinPoint.getSignature()：
        // 该对象包含了连接点的相关信息(个人理解处于类层面)，例如方法名、参数类型等(不是对象，是对象类型)。具体返回类型取决于连接点的类型，通常为MethodSignature对象。
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();//方法签名对象

        //获取到当前被拦截的方法上的数据库操作类型:value
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        OperationType value = autoFill.value();

        //获取到当前被拦截的方法的参数--实体对象（这里是Employee、Category对象）
        Object[] args = joinPoint.getArgs();//参数实体对象
        Object entity = args[0];//注：一般把想要使用的参数实体对象放在方法第一个参数

        //准备赋值的数据:时间、登陆的id
        LocalDateTime now = LocalDateTime.now();
        Long empId = BaseContext.getCurrentId();

        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        if (value == OperationType.INSERT) {
            //为4个公共字段赋值
//            Employee entity1 = (Employee) entity;
//            entity1.setUpdateUser(empId);
//            entity1.setUpdateTime(now);
//            entity1.setCreateTime(now);
//            entity1.setCreateUser(empId);
            try {
                //获取entity（Employee、Category）的方法：AutoFillConstant.SET_CREATE_TIME、传入的参数类型：LocalDateTime.class
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, empId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, empId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (value == OperationType.UPDATE) {
            //为4个公共字段赋值
//            Employee entity1 = (Employee) entity;
//            entity1.setUpdateUser(empId);
//            entity1.setUpdateTime(now);

            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, empId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
