package com.zj.auth.client;


import com.zj.common.result.Result;
import org.springframework.stereotype.Component;

/**
 * @author clinflash_zhengjie
 */
@Component
public class UserFeignHandler implements UserFeign{

    @Override
    public String getUserDetail(Integer userId) {
        return null;
    }

    @Override
    public Result<Object> queryUserByPhone(String phone) {
        return null;
    }

    @Override
    public Result<Object> queryById(long parseLong) {
        return null;
    }

    @Override
    public Result<Object> queryUserByAccount(String username) {
        return null;
    }
}
