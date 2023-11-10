package com.zj.gateway.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 14:59
 *
 * 使用时改变配置文件中的key-resolver的对象名
 *
 */
@Configuration
public class LimitFilter {

    /**
     * 按照 Path 访问次数限流
     *
     */
    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getPath()
                        .toString()
        );
    }

    /**
     * 按照自定义userId限流
     *
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(
                Objects.requireNonNull(exchange.getRequest().getQueryParams().getFirst("userId"))
        );
    }

    /**
     * 根据IP限流
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
                exchange.getRequest()
                        .getRemoteAddress()
                        .getHostName()
        );
    }

    /**
     * 根据token限流
     */
    @Bean
    public KeyResolver tokenKeyResolver() {
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                return Mono.just(exchange.getRequest().getQueryParams().getFirst("token"));
            }
        };
    }
}
