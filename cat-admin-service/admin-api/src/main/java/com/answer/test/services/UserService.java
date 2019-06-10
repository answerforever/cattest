package com.answer.test.services;

import com.answer.test.dto.FrontUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户管理接口
 */
@Api(value = "用户管理")
@RequestMapping(value = "user")
public interface UserService {
    @ApiOperation(value = "查询用户", response = Integer.class)
    @PostMapping(value = "getuserinfo")
    FrontUser getUserInfo(Integer id);

}