package com.answer.test.request;

import java.util.Map;


public class Service3RdContext {
    /**
     * key为url
     */
    public static final String URL = "url";

    private static ThreadLocal<Map<String, String>> threadLocalMap = new ThreadLocal<>();

    public static Map<String, String> getContext() {
        return threadLocalMap.get();
    }

    public static void setHeaderContext(Map<String, String> context) {
        threadLocalMap.set(context);
    }

    public static void clear() {
        threadLocalMap.set(null);
    }
}