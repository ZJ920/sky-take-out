package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.AdminException;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    //username：账号已存在会抛出SQLIntegrityConstraintViolationException，捕获该异常并处理
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result SQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException ex){
        //ex.getMessage() = Duplicate entry '' for key 'employee.idx_username'
        String message = ex.getMessage();
        log.error("异常信息：{}", message);
        String userName = message.split(" ")[2];
        return Result.error(userName + MessageConstant.ACCOUNT_ALREADY_EXISTS);
    }

    @ExceptionHandler(AdminException.class)
    public Result adminException(AdminException a){
        log.error("异常信息：{}", a.getMessage());
        return Result.error(a.getMessage());
    }

}
