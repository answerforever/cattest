package com.answer.test.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "Response", description = "Response对象")
public class Response<T> implements Serializable {

    @ApiModelProperty(value = "成功标识")
    public boolean success = true;

    @ApiModelProperty(value = "响应体header对象")
    public ResponseHeader header;

    @ApiModelProperty(value = "业务对象")
    public T data;

}
