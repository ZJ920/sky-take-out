package com.sky.exception;

/**
 * 员工权限校验失败
 */
public class AdminException extends BaseException{
    public AdminException(){}
    public AdminException(String msg){
        super(msg);
    }
}
