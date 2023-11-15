package com.zj.gateway.context;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 11:13
 */
public interface GatewayContextExtraData<T> {

    /**
     * get context extra data
     * @return
     */
    T getData();
}
