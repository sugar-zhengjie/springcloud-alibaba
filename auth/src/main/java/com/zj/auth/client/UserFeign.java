package com.zj.auth.client;

import com.zj.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user",fallback = UserFeignHandler.class)
public interface UserFeign {
    @GetMapping("/user/getDetail/{userId}")
    String getUserDetail(@PathVariable Integer userId);

    Result<Object> queryUserByPhone(String phone);

    Result<Object> queryById(long parseLong);

    Result<Object> queryUserByAccount(String username);
}
