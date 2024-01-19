package com.syy.usercenterbackend;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class UserCenterBackendApplicationTests {

    // 测试加密


    @Test
    void contextLoads() throws NoSuchAlgorithmException {
        String newPassword = DigestUtils.md5DigestAsHex(("abcd" + "mypassword").getBytes());
        System.out.println(newPassword);
    }

}

// UserCenterBackendApplicationTests类不用Run with的原因
// @Test testDigest为什么会报错？(2023/12/3)  莫名其妙又好了