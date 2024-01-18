package com.syy.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syy.usercenterbackend.common.ErrorCode;
import com.syy.usercenterbackend.exception.BusinessException;
import com.syy.usercenterbackend.model.domain.User;
import com.syy.usercenterbackend.service.UserService;
import com.syy.usercenterbackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.syy.usercenterbackend.constant.UserConstant.LOGIN_STATE;

/**
* @author Song
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-12-01 15:50:13
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    private UserMapper userMapper;    // 注入Mapper

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "SYY";

    /**
     * 用户登录态键 (key)
     */
    // 将其移至接口类UserService中
    // public static final String LOGIN_STATE = "loginState";

    @Override
    public long userRegister(String account, String password, String checkPassword, String planetCode) {
        // 1. 校验（使用Java类库apache common utils）
        // 1.1 字符串非空&长度校验
        // StringUtils.isAllBlank: 同时判断这几个字符串是否为空
        if (StringUtils.isAllBlank(account, password, checkPassword, planetCode))
            throw new BusinessException(ErrorCode.PARAM_ERROR, "参数为空！");
        // 星球编号长度不能大于5
        if (account.length() < 4 || password.length() < 8 || checkPassword.length() < 8 || planetCode.length() > 5)
            throw new BusinessException(ErrorCode.PARAM_ERROR, "输入长度异常！");

        // 1.2 账户不能包含特殊字符（有问题）
        // String validPattern = "\\pP|\\pS|\\s+";    // 正则表达式
        String validPattern = "^[a-zA-Z0-9]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(account);
        if (!matcher.find())
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账号不能包含特殊字符！");


        // 1.3 密码和校验密码相同
        if (!password.equals(checkPassword))
            throw new BusinessException(ErrorCode.PARAM_ERROR, "两次输入的密码不一致！");

        // 1.4 账户不能重复：在数据库中查找是否有相同账户的人
        // 创建查询条件类QueryWrapper
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 在数据库中中查找"account"列中的值为account的记录
        queryWrapper.eq("account", account);
        // 查询结果的数量
        // long count = this.count(queryWrapper);
        long count = userMapper.selectCount(queryWrapper);    // 使用Mapper
        if (count > 0 )
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账号已被注册！");

        // 星球编号不能重复
        QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
        // 在数据库中中查找"account"列中的值为account的记录
        queryWrapper2.eq("planetCode", planetCode);
        // 查询结果的数量
        // long count = this.count(queryWrapper);
        long count2 = userMapper.selectCount(queryWrapper2);    // 使用Mapper
        if (count2 > 0 )
            throw new BusinessException(ErrorCode.PARAM_ERROR, "星球编号已被注册！");

        // 2. 密码加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        System.out.println(newPassword);

        // 3. 插入数据
        User user = new User();
        user.setAccount(account);
        user.setPassword(newPassword);
        user.setPlanetCode(planetCode);
        user.setUsername("No.89757(Default)");
        boolean save = this.save(user);    // 将user对象插入数据库

        return save ? user.getId() : -1;
    }

    @Override
    public User userLogin(String account, String password, HttpServletRequest request) {
        // 1. 校验（使用Java类库apache common utils）
        // 1.1 字符串非空&长度校验
        if (StringUtils.isAllBlank(account, password))
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAM_ERROR, "输入为空！");
        if (account.length() < 4 || password.length() < 8)
            throw new BusinessException(ErrorCode.PARAM_ERROR, "输入长度异常！");

        // 1.2 账户不能包含特殊字符（有问题）
        String validPattern = "^[a-zA-Z0-9]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(account);
        if (!matcher.find())
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账号包含特殊字符！");

        // 2. 在数据库中查询用户是否存在
        // 2.1 密码加密
        String newPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 查询条件1：account是否匹配
        queryWrapper.eq("account", account);
        // 查询条件2：password是否匹配
        queryWrapper.eq("password", newPassword);
        // 将查询结果返回至User
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("Login failed, account cannot match password!");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "登陆失败！");
        }

        // 3. 为待返回的用户信息脱敏（Alt + Enter: Generate All setter with default values）
        User cleanUser = getCleanUser(user);

        // 4. 记录用户的登录态
        request.getSession().setAttribute(LOGIN_STATE, cleanUser);

        return cleanUser;
    }

    /**
     * 用户脱敏
     * @param user
     * @return 脱敏后的用户信息
     */
    @Override
    public User getCleanUser(User user){
        if (user == null)
            throw new BusinessException(ErrorCode.PARAM_ERROR, "输入参数为空！");
        User cleanUser = new User();
        cleanUser.setId(user.getId());
        cleanUser.setUsername(user.getUsername());
        cleanUser.setAccount(user.getAccount());
        cleanUser.setAvatarUrl(user.getAvatarUrl());
        cleanUser.setGender(user.getGender());
//        cleanUser.setPassword("");
        cleanUser.setPhone(user.getPhone());
        cleanUser.setEmail(user.getEmail());
        cleanUser.setStatus(0);
        cleanUser.setCreateTime(user.getCreateTime());
        cleanUser.setUserRole(user.getUserRole());
        cleanUser.setPlanetCode(user.getPlanetCode());    // 星球编号
//        cleanUser.setUpdateTime(new Date());
//        cleanUser.setIsDeleted(0);
        return cleanUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除当前用户的登录态
        request.getSession().removeAttribute(LOGIN_STATE);
        return 1;
    }
}




