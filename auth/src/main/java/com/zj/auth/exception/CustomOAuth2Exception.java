package com.zj.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义Oauth异常拦截处理器
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 16:19
 */
@JsonSerialize(using = CustomOAuth2ExceptionSerializer.class)
public class CustomOAuth2Exception extends OAuth2Exception {

    public CustomOAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomOAuth2Exception(String msg) {
        super(msg);
    }
}