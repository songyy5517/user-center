package com.syy.usercenterbackend.service;
import java.util.Date;

import com.syy.usercenterbackend.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试
 */

@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    void testAddUser(){
        // 创建User对象，并初始化
        User user = new User();

        // 使用插件（GenerateAllSetter）进行对象赋值
        user.setUsername("Test SYY");
        user.setAccount("123");
        user.setAvatarUrl("https://himg.bdimg.com/sys/portrait/item/public.1.50e08db.MWxNPkC-a4VPexAYlutkKA.jpg");
        user.setGender(0);
        user.setPassword("123456");
        user.setPhone("123");
        user.setEmail("456");

        boolean result = userService.save(user);// 往数据库中加入一条数据
        System.out.println(user.getId());
        assertTrue(result);    // 断言：判断result是否为true，以判断程序是否正常运行
    }

    @Test
    void userRegister(){
        String account = "SongYuyuan01";
        String password = "1234561234";
        String checkPassword = "1234561234";
        String planetCode = "1";
        long result = userService.userRegister(account, password, checkPassword, planetCode);
        System.out.println("results: " + result);
        // Assertions.assertEquals(-1, result);    // (预期值，实际值)
        Assertions.assertTrue(result > -1);
    }

}