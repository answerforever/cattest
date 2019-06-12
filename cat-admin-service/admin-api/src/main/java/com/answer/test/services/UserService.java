package com.answer.test.services;

import com.answer.test.dto.FrontUser;
import com.answer.test.request.Request;
import com.answer.test.request.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户管理接口
 */
@Api(value = "用户管理")
@RequestMapping(value = "user")
public interface UserService {
    @ApiOperation(value = "查询用户", response = FrontUser.class)
    @PostMapping(value = "/front/info")
    Response<FrontUser> getUserInfo(@RequestBody Request<Integer> request);

    @ApiOperation(value = "检查用户", response = Integer.class)
    @PostMapping(value = "/font/check")
    Response<Integer> checkError(@RequestBody Request<Integer> request);

}
