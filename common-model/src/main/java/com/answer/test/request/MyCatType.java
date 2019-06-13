package com.answer.test.request;

/**
 * @author yueqi.shi
 * @date 2018/11/23 00:58
 */
public enum MyCatType {

    T_ZUUL("ZUUL", "网关调用"),

    T_REST("REST", "服务调用"),
    REST_METHOD("REST_METHOD", "服务调用方法"),
    REST_REQ("REST_REQ", "服务调用请求"),
    REST_RESP("REST_RESP", "服务调用结果"),
    REST_EXP("REST_EXP", "服务调用异常"),
    REST_MSG("REST_MSG", "服务调用msg"),

    T_SERVICE_3RD("SERVICE_3RD", "第三方调用"),
    SERVICE_REQ("SERVICE_REQ", "第三方调用请求"),
    SERVICE_RESP("SERVICE_RESP", "第三方调用结果"),
    SERVICE_MSG("SERVICE_MSG", "第三方调用msg"),
    SERVICE_URL("SERVICE_URL", "第三方调用url"),

    T_SERVICE_3RD_NET("SERVICE_3RD_NET", ".NET第三方调用"),

    T_SQL("SQL", "数据库操作"),
    SQL_METHOD("SQL_METHOD", "数据库操作方法名"),
    SQL_DUMP("SQL_DUMP", "数据库操作sql语句"),

    T_REDIS("REDIS", "redis缓存操作"),

    T_ES("ES", "es操作"),

    T_EXECUTOR("EXECUTOR", "异步线程")
    ;

    private String code;

    private String desc;

    MyCatType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static MyCatType valueOfCode(String code) {
        if (code == null) {
            return null;
        }

        for (MyCatType yqnCatType : MyCatType.values()) {
            if (yqnCatType.getCode().equals(code)) {
                return yqnCatType;
            }
        }

        return null;
    }
}