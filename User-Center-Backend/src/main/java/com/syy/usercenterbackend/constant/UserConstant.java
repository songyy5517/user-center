package com.syy.usercenterbackend.constant;

/**
 * 用户常量
 */
public interface UserConstant {
    // 成员默认是public static

    /**
     * 用户登录态键 (key)，用于设置Session.Attribute
     */
    String LOGIN_STATE = "loginState";

    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;
    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;

}
