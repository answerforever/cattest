package com.answer.test.request;


import org.springframework.beans.BeanUtils;


public class RPCContext {

    /**
     * 构建request对象，header从threadlocal中获取
     * @param model
     * @return
     */
    public static <T> Request<T> createRequest(T model) {
        Request<T> request = new Request<T>();
        Request.Header header = RequestHeaderContext.getHeaderContext();
        Request.Header requestHeader = new Request.Header();

        header = header == null ? new Request.Header() : header;
        BeanUtils.copyProperties(header, requestHeader);


//        requestHeader.setxAppId(AppConstantsConfig.APP_ID);
//        requestHeader.setxClientIp(IPUtils.getServerIP());

        request.setHeader(requestHeader);
        request.setModel(model);

        return request;
    }

}