package com.answer.test.service;

import com.answer.test.dto.AuthGroupUser;
import com.answer.test.manager.AuthManager;
import com.answer.test.request.Request;
import com.answer.test.request.Response;
import com.answer.test.services.AuthService;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController implements AuthService {
    @Autowired
    private AuthManager authManager;

    private static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private LoadBalancerClient loadBalancer;
    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "getauthinfo")
    public Response<AuthGroupUser> getAuthGroupUser(@RequestBody Request<Integer> request) {
        AuthGroupUser authGroupUser = null;

        Integer id =request.getModel();
        LOGGER.info("getAuthGroupUser入参:" + id);

        try {
            authGroupUser = authManager.getAuthById(id);

        } catch (Exception ex) {
            LOGGER.error("getAuthGroupUser出现错误", ex);
        }

        LOGGER.info("getAuthGroupUser结束:" + id);
        Response<AuthGroupUser> response=new Response<>();
        response.setData(authGroupUser);
        return response;
    }

    /**
     * 获取所有服务
     */
    @PostMapping("/services")
    public Object services() {
        return discoveryClient.getInstances("answer-admin1");
    }

    /**
     * 从所有服务中选择一个服务（轮询）
     */
    @PostMapping("/discover")
    public Object discover() {
        String url="";
        ServiceInstance serviceInstance = loadBalancer.choose("answer-admin1");
        if(serviceInstance!=null)
        {
            url = serviceInstance.getUri().toString();
        }
        return url;
    }

    @PostMapping("/dc")
    public String dc() {
        String services = "Services: " + discoveryClient.getServices();
        System.out.println(services);
        return services;
    }

}
