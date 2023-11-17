package com.zj.gateway.handler;

import java.nio.charset.Charset;

import com.zj.common.enums.ResultCodeEnum;
import com.zj.common.result.Result;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import cn.hutool.json.JSONUtil;
import reactor.core.publisher.Mono;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/17 14:24
 */
@Component
public class AuthServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set("Access-Control-Allow-Origin", "*");
        response.getHeaders().set("Cache-Control", "no-cache");
        String body = JSONUtil.toJsonStr(Result.error(ResultCodeEnum.UNAUTHORIZED));

        if (e instanceof Exception) {
            body = JSONUtil.toJsonStr(Result.error(ResultCodeEnum.TENANT_NOT_FOUND));
        }

        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
        return response.writeWith(Mono.just(buffer)).doFinally(s -> {
            DataBufferUtils.release(buffer);
        });
    }

}
