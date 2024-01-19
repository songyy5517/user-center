package com.syy.usercenterbackend.model.request;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -3743692093088166128L;

    private String account;
    private String password;
    // private HttpServletRequest request;

}
