package com.answer.test.request;


public class RequestHeaderContext {

    private static ThreadLocal<Request.Header> threadLocalHeaderMap = new ThreadLocal<>();

    public static Request.Header getHeaderContext() {
        return threadLocalHeaderMap.get();
    }

    public static void setHeaderContext(Request.Header headerContext) {
        threadLocalHeaderMap.set(headerContext);
    }

    public static void clear() {
        threadLocalHeaderMap.set(null);
    }

}
