package feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MyFeignClientsConfiguration {
    private static Logger LOGGER = LoggerFactory.getLogger(MyFeignClientsConfiguration.class);
    @Bean
    public Feign.Builder feignInvocationHandlerFactory() {
        return Feign.builder().invocationHandlerFactory(new MyInvocationHandlerFactory());
    }

    @Bean
    public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
                              SpringClientFactory clientFactory) {


        LOGGER.info("MyFeignClient构建开始。。。");

        return new LoadBalancerFeignClient(new MyFeignClient(null, null),
            cachingFactory, clientFactory);
    }

}