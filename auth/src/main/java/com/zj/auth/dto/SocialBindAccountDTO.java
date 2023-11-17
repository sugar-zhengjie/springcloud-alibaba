package com.zj.auth.dto;

import lombok.Data;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/17 11:20
 */
@Data
public class SocialBindAccountDTO {

    /**
     * 要绑定的加密后的socialId
     */
    private String socialKey;

    /**
     * 账号
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

}
