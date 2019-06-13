package com.answer.test.aop;

import com.answer.test.request.MyCatContext;
import com.answer.test.request.Request;
import com.answer.test.request.RequestHeaderContext;
import com.dianping.cat.Cat;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Aspect
@EnableAspectJAutoProxy
@Configuration
public class CatMsgIdAspectBean {

    private static Logger LOGGER = LoggerFactory.getLogger(CatMsgIdAspectBean.class);

    /**
     * 定义拦截规则：拦截com.answer.test包下面的所有类中，有@RequestMapping注解的方法。
     */
    @Pointcut("execution(* com.answer.test..*(..)) && @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void controllerMethodPointcut(){};

    @Around("controllerMethodPointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Request request = null;
        Request.Header header = null;

        Object[] args = pjp.getArgs();
        request = (Request) args[0];

        if (request == null) {
            //参数为null，抛出异常
        }

        header = request.getHeader();
        if (header == null) {
            //header为null,抛出异常
        }

        //Cat context
        if (header.getMyCatContext() == null) {
            header.setMyCatContext(new MyCatContext());
        } else {
            Cat.logRemoteCallServer(header.getMyCatContext());
        }

        //Header存入ThreadLocal
        RequestHeaderContext.setHeaderContext(request.getHeader());

        Object proceed = pjp.proceed();
        return proceed;
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
}
