package com.zj.auth.dto;

import lombok.Data;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/17 11:20
 */
@Data
public class LogoutDTO {

    private String refreshToken;
}
