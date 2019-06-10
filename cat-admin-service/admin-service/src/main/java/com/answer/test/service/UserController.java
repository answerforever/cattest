package com.answer.test.service;

import com.answer.test.dto.FrontUser;
import com.answer.test.manager.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserManager userManager;

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/front/info", method = RequestMethod.POST)
    public FrontUser getUserInfo(Integer id) {

        LOGGER.info("getUserInfo开始，入参:" + id);

        FrontUser frontUser = null;
        try {
            frontUser = userManager.getUserById(id);

            int value = 2 / 0;
        } catch (Exception ex) {
            LOGGER.error("getUserInfo出现错误", ex);
        }

        LOGGER.info("getUserInfo返回参数：" + id);

        return frontUser;
    }

    @PostMapping(value = "/font/check")
    public int checkError(Integer id) {
        int result = 0;
        LOGGER.info("check开始,入参：" + id);
        try {
            FrontUser frontUser = null;
            frontUser.setId(id);
        } catch (Exception ex) {
            LOGGER.error("check出现错误", ex);
        }
        return result;
    }
}
