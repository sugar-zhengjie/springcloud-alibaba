package com.zj.gateway.context;

import org.springframework.web.server.ServerWebExchange;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 11:13
 */
public interface ContextExtraDataGenerator<T> {

    /**
     * generate context extra data
     * @param serverWebExchange
     * @return
     */
    GatewayContextExtraData<T> generateContextExtraData(ServerWebExchange serverWebExchange);

}
