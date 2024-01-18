/**
 * 专门编写用户相关服务的接口
 */
package com.syy.usercenterbackend.service;
import com.syy.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Song
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-12-01 15:50:13
 */
public interface UserService extends IService<User> {
    /**
     * 用户登录态键 (key)
     */
    // String LOGIN_STATE = "loginState";  该常量被放在常量接口中了
    /**
     * 用户注册
     *
     * @param account       用户账号
     * @param password      用户密码
     * @param checkPassword 校验密码
     * @param planetCode    星球编号
     * @return 注册用户的id
     */
    long userRegister(String account, String password, String checkPassword, String planetCode);

    /**
     * 用户登录
     *
     * @param account  用户账号
     * @param password 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String account, String password, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param user
     * @return 脱敏后的用户信息
     */
    User getCleanUser(User user);


    /**
     * 用户注销（退出登录）
     * @param request
     */
    int userLogout(HttpServletRequest request);
}
