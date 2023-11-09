package com.zj.constant;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 14:24
 */
public enum AuthGrantType {
    /**
     * 用户名+密码方式获取认证
     */
    PASSWORD("password"),
    /**
     * 通过refresh token更新认证
     */
    REFRESH_TOKEN("refresh_token");
    private final String code;

    AuthGrantType(String code) {
        this.code = code;
    }

}
