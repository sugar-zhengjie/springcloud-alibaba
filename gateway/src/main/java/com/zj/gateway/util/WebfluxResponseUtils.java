package com.zj.gateway.util;

import cn.hutool.json.JSONUtil;
import com.zj.common.result.Result;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 11:02
 *
 * 处理WebFlux响应
 */
public class WebfluxResponseUtils {

    public static Mono<Void> responseWrite(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set("Access-Control-Allow-Origin", "*");
        response.getHeaders().set("Cache-Control", "no-cache");
        String body= JSONUtil.toJsonStr(Result.error(message));
        DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
        return response.writeWith(Mono.just(buffer)).doFinally(s -> {
            DataBufferUtils.release(buffer);
        });
    }
}
