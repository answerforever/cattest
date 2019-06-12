package com.answer.test.aspect;

import com.answer.test.constants.CatMsgConstants;
import com.answer.test.request.*;
import com.dianping.cat.Cat;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.UUID;

@Aspect
@EnableAspectJAutoProxy
@Configuration
public class CatMsgIdAspectBean {

    private static Logger LOGGER = LoggerFactory.getLogger(CatMsgIdAspectBean.class);

    /**
     * 定义拦截规则：拦截com.xjj.web.controller包下面的所有类中，有@RequestMapping注解的方法。
     */
    @Pointcut("execution(* com.answer.test..*(..)) && @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void controllerMethodPointcut(){};

    // && (@annotation(restMethod))
    //@Around(value = "execution(* com.answer.test..*(..))")
    @Around("controllerMethodPointcut()")
    public Response around(ProceedingJoinPoint pjp) throws Throwable {
//        createMessageTree();

        Request request=null;
        Request.Header header=null;
        Response response=null;
        Object[] args = pjp.getArgs();
        request=(Request) args[0];

        if(request==null)
        {
            //参数为null，抛出异常

        }

        header=request.getHeader();
        if(header==null)
        {
            //header为null,抛出异常

        }

        String traceId="";
        //Cat context
        MyCatContext currentCatContext = header.getMyCatContext();
        if (currentCatContext == null) {
            currentCatContext = new MyCatContext();

            header.setMyCatContext(currentCatContext);
            Cat.logRemoteCallClient(currentCatContext);
            traceId = currentCatContext.getProperty(Cat.Context.ROOT);

            LOGGER.info("当前TraceId："+traceId);

        } else {
            //Cat.logRemoteCallServer(currentCatContext);
        }

        //Header存入ThreadLocal
        RequestHeaderContext.setHeaderContext(request.getHeader());

//        String xAppId = "";
//        String xTraceId = ;
//        if (request.getHeader() != null && request.getHeader().getxAppId() != null) {
//            xAppId = request.getHeader().getxAppId();
//        }
//        if (request.getHeader() != null && request.getHeader().getxTraceId() != null) {
//            xTraceId = request.getHeader().getxTraceId();
//        }
        ResponseHeader responseHeader = new ResponseHeader("", traceId);
        response = (Response) pjp.proceed();
        response.setHeader(responseHeader);
        return response;
    }

    /**
     * 统一设置消息编号的messageId
     */
    private void createMessageTree() {
        MyCatContext context = new MyCatContext();
        Cat.logRemoteCallClient(context);
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        requestAttributes.setAttribute(Cat.Context.PARENT, context.getProperty(Cat.Context.PARENT), 0);
//        requestAttributes.setAttribute(Cat.Context.ROOT, context.getProperty(Cat.Context.ROOT), 0);
//        requestAttributes.setAttribute(Cat.Context.CHILD, context.getProperty(Cat.Context.CHILD), 0);
//        requestAttributes.setAttribute(CatMsgConstants.APPLICATION_KEY, Cat.getManager().getDomain(), 0);
    }

    private String getTraceId()
    {
        return applicationName+"-"+UUID.randomUUID().toString().replace("-","");
    }

    @Value("${spring.application.name}")
    private String applicationName;
}
