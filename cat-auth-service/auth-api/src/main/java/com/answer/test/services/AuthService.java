package com.answer.test.services;


import com.answer.test.dto.AuthGroupUser;
import com.answer.test.request.Request;
import com.answer.test.request.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "权限管理")
@RequestMapping(value = "auth")
public interface AuthService {
    @ApiOperation(value = "查询权限", response = AuthGroupUser.class)
    @PostMapping(value = "getauthinfo")
    Response<AuthGroupUser> getAuthGroupUser(@RequestBody Request<Integer> id);

}
