package com.answer.test.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "ResponseHeader", description = "ResponseHeader")
public class ResponseHeader {
    @ApiModelProperty(value = "调用来源app id")
    private String xAppId;

    @ApiModelProperty(value = "调用链")
    private String xTraceId;

    public ResponseHeader() {
    }

    public ResponseHeader(String xAppId, String xTraceId) {
        this.xAppId = xAppId;
        this.xTraceId = xTraceId;
    }

    public String getxAppId() {
        return xAppId;
    }

    public void setxAppId(String xAppId) {
        this.xAppId = xAppId;
    }

    public String getxTraceId() {
        return xTraceId;
    }

    public void setxTraceId(String xTraceId) {
        this.xTraceId = xTraceId;
    }
}