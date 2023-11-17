package com.zj.auth.dto;

import lombok.Data;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/17 11:24
 */
@Data
public class SocialBindMobileDTO {

    /**
     * 要绑定的加密后的socialId
     */
    private String socialKey;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 短信验证码模板编码
     */
    private String smsCode;

    /**
     * 短信验证码
     */
    private String code;

}
