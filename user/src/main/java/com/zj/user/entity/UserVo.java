package com.zj.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/13 13:21
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

    private String userId;

    private String username;

    private String password;

    private String email;

    private String mobile;

    private Integer status;

    private String cloudId;

    private List<Integer> roles;

    private String createdDate;
}
