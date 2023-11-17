package com.zj.auth.client;

import com.zj.common.result.Result;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/17 10:39
 */
public interface JustAuthFeign {
    Result<Object> userBindQuery(long parseLong);
}
