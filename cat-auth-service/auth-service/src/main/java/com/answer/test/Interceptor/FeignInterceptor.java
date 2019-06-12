package com.answer.test.Interceptor;

import com.answer.test.aspect.CatMsgIdAspectBean;
import com.answer.test.constants.CatMsgConstants;
import com.dianping.cat.Cat;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

//@Component
//public class FeignInterceptor implements RequestInterceptor {
//
//    private static Logger LOGGER = LoggerFactory.getLogger(CatMsgIdAspectBean.class);
//
//    @Override
//    public void apply(RequestTemplate requestTemplate) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        String rootId = requestAttributes.getAttribute(Cat.Context.ROOT, 0).toString();
//        String childId = requestAttributes.getAttribute(Cat.Context.CHILD, 0).toString();
//        String parentId = requestAttributes.getAttribute(Cat.Context.PARENT, 0).toString();
//        requestTemplate.header(Cat.Context.ROOT, rootId);
//        requestTemplate.header(Cat.Context.CHILD, childId);
//        requestTemplate.header(Cat.Context.PARENT, parentId);
//        requestTemplate.header(CatMsgConstants.APPLICATION_KEY, Cat.getManager().getDomain());
//        LOGGER.info(" 开始Feign远程调用 : " + requestTemplate.method() + " 消息模型 : rootId = " + rootId +
//                " parentId = " + parentId + " childId = " + childId);
//    }
//
//}
