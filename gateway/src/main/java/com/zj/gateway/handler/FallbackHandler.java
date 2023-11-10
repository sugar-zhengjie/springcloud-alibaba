package com.zj.gateway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 15:17
 */
@Component
public class FallbackHandler implements HandlerFunction<ServerResponse> {

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

}
