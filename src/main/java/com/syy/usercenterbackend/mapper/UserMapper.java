package com.syy.usercenterbackend.mapper;

import com.syy.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Song
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2023-12-01 15:50:13
* @Entity generator.domain.User
*/

// MyBatis-plus帮我们定义了增删改查的方法，我们只需继承BaseMapper类即可
public interface UserMapper extends BaseMapper<User> {

}




