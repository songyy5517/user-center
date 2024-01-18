package com.syy.usercenterbackend.common;

/**
 * 返回工具类
 */
public class ResultUtils {
    /**
     * 成功
     * @param result
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T result){
        return new BaseResponse<T>(0, result, "Success!", "");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> fail(ErrorCode errorCode){
        return new BaseResponse<T>(errorCode);
    }

    public static <T> BaseResponse<T> fail(ErrorCode errorCode, String message, String description){
        return new BaseResponse<T>(errorCode, message, description);
    }

    public static <T> BaseResponse<T> fail(int errorCode, String message, String description){
        return new BaseResponse<T>(errorCode, message, description);
    }

    public static <T> BaseResponse<T> fail(ErrorCode errorCode, String description){
        return new BaseResponse<T>(errorCode, errorCode.getMessage(), description);
    }


}
