package com.zj.workflow.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "返回响应数据")
public class RestMessage {

    @ApiModelProperty(value = "错误信息")
    private String message;
    @ApiModelProperty(value = "状态码")
    private String code;
    @ApiModelProperty(value = "返回的数据")
    private Object data;

    public static  RestMessage success(String message, Object data){
        RestMessage RestMessage = new RestMessage();
        RestMessage.setCode("200");
        RestMessage.setMessage(message);
        RestMessage.setData(data);
        return RestMessage;
    }

    public static  RestMessage fail(String message, Object data){
        RestMessage RestMessage = new RestMessage();
        RestMessage.setCode("500");
        RestMessage.setMessage(message);
        RestMessage.setData(data);
        return RestMessage;
    }
}
