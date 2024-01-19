package com.syy.usercenterbackend.exception;

import com.syy.usercenterbackend.common.BaseResponse;
import com.syy.usercenterbackend.common.ErrorCode;
import com.syy.usercenterbackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice    // Spring AOP (切面)，在代码执行（调用方法）前后做一些额外处理。
@Slf4j    // lombok注解，提供日志对象log
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)    // 捕获项目中所有BusinessException类的异常
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("BusinessException " + e.getMessage(), e);
        return ResultUtils.fail(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)    // 捕获RuntimeException类的异常
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("RuntimeException:", e);    // 记录日志；在项目中任何出现RuntimeException的地方，都会被记录。
        return ResultUtils.fail(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }

}
