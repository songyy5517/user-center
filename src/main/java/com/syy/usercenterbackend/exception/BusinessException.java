package com.syy.usercenterbackend.exception;

import com.syy.usercenterbackend.common.ErrorCode;

/**
 * 自定义异常类
 */
public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = -6085029240943955550L;
    // 对原有的RuntimeException类扩充了code和description属性
    private final int code;
    private final String description;    // 给前端的提示（Message的原因）

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
