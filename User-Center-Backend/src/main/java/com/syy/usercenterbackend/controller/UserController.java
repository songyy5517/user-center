package com.syy.usercenterbackend.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syy.usercenterbackend.common.BaseResponse;
import com.syy.usercenterbackend.common.ErrorCode;
import com.syy.usercenterbackend.common.ResultUtils;
import com.syy.usercenterbackend.exception.BusinessException;
import com.syy.usercenterbackend.model.domain.User;
import com.syy.usercenterbackend.model.request.UserLoginRequest;
import com.syy.usercenterbackend.model.request.UserRegisterRequest;
import com.syy.usercenterbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import static com.syy.usercenterbackend.constant.UserConstant.ADMIN_ROLE;
import static com.syy.usercenterbackend.constant.UserConstant.LOGIN_STATE;

/**
 * 实际接收前端请求的层
 */
@RestController
@RequestMapping("/user")
// @CrossOrigin(origins = {"http://106.53.192.192"}, allowCredentials = "true")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        // 1. 校验
        if (userRegisterRequest == null)
            // return ResultUtils.fail(ErrorCode.PARAM_ERROR);
            throw new BusinessException(ErrorCode.PARAM_ERROR);

        String account = userRegisterRequest.getAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(account, password, checkPassword, planetCode))
            throw new BusinessException(ErrorCode.PARAM_ERROR, "输入参数为空！");

        // 2. 调用业务逻辑层进行注册
        // return userService.userRegister(account, password, checkPassword, planetCode);
        long id = userService.userRegister(account, password, checkPassword, planetCode);
        return ResultUtils.success(id);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        // 1. 校验
        if (userLoginRequest == null)
            throw new BusinessException(ErrorCode.NULL_ERROR);

        String account = userLoginRequest.getAccount();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(account, password))
            throw new BusinessException(ErrorCode.PARAM_ERROR, "输入参数为空");

        // 2. 调用业务逻辑层进行登录
        User user = userService.userLogin(account, password, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销/退出登录
     * @param request
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null)
            throw new BusinessException(ErrorCode.NULL_ERROR);
        int i = userService.userLogout(request);
        return ResultUtils.success(i);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request){
        // 权限校验：仅管理员课查询
//        User user = (User) request.getSession().getAttribute(LOGIN_STATE);
//        if (user == null || user.getRole() != ADMIN_ROLE)    // 将1定义成常量
//            return null;
        if (!isAdmin(request))
            throw new BusinessException(ErrorCode.NO_AUTH);

        // 定义查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username", username);    // like: 返回包含字符串的记录，并不要求完全一致
        }

        List<User> userList = userService.list(queryWrapper);  //  MyBatis-plus自带的方法
        userList = userList.stream().map(user -> userService.getCleanUser(user)).collect(Collectors.toList());

        return ResultUtils.success(userList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request){
        // 权限校验：仅管理员可删除用户
//        User user = (User) request.getSession().getAttribute(LOGIN_STATE);
//        if (user == null || user.getRole() != ADMIN_ROLE)    // 将1定义成常量
//            return false;

        if (!isAdmin(request))
            throw new BusinessException(ErrorCode.NO_AUTH);

        // 校验
        if (id <= 0)
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户id不存在！");

        boolean result = userService.removeById(id);    // MyBatis-plus中的逻辑删除方法
        return ResultUtils.success(result);
    }

    /**
     * 校验用户是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        // 权限校验：仅管理员可删除用户
        User user = (User) request.getSession().getAttribute(LOGIN_STATE);
        if (user == null || user.getUserRole() != ADMIN_ROLE)    // 将1定义成常量
            return false;
        return true;
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        // 从Request请求中获取用户的登录态
        User user = (User) request.getSession().getAttribute(LOGIN_STATE);
        if (user == null)
            throw new BusinessException(ErrorCode.NO_LOGIN);

        // 获取用户的ID，并根据ID查询用户信息
        long id = user.getId();
        // todo 校验用户是否合法
        User user1 = userService.getById(id);

        // 返回脱敏的用户信息
        User cleanUser = userService.getCleanUser(user1);
        return ResultUtils.success(cleanUser);
    }

}
