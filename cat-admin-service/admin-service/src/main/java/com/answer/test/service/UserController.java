package com.answer.test.service;

import com.answer.test.dto.FrontUser;
import com.answer.test.manager.UserManager;
import com.answer.test.request.Request;
import com.answer.test.request.Response;
import com.answer.test.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController implements UserService {

    @Autowired
    private UserManager userManager;

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/front/info")
    public Response<FrontUser> getUserInfo(@RequestBody Request<Integer> request) {

        Integer id=request.getModel();
        LOGGER.info("getUserInfo开始，入参:" + id);

        FrontUser frontUser = null;
        try {
            frontUser = userManager.getUserById(id);

            //int value = 2 / 0;
        } catch (Exception ex) {
            LOGGER.error("getUserInfo出现错误", ex);
        }

        LOGGER.info("getUserInfo返回参数：" + id);

        Response<FrontUser> response=new Response<>();
        response.setData(frontUser);
        return response;
    }

    @PostMapping(value = "/font/check")
    public Response<Integer> checkError(@RequestBody Request<Integer> request) {
        int result = 0;
        Integer id=request.getModel();
        LOGGER.info("check开始,入参：" + id);
        try {
            FrontUser frontUser = null;
            frontUser.setId(id);
        } catch (Exception ex) {
            LOGGER.error("check出现错误", ex);
        }
        Response<Integer> response=new Response<>();
        response.setData(result);
        return response;
    }
}
