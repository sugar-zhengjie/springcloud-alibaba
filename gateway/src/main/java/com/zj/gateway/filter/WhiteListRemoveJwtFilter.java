package com.zj.gateway.filter;

import java.net.URI;
import java.util.List;

import com.zj.gateway.constant.AuthConstant;
import com.zj.gateway.properties.AuthUrlWhiteListProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 白名单路径访问时需要移除JWT请求头
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 13:38
 */
@AllArgsConstructor
@Component
public class WhiteListRemoveJwtFilter implements WebFilter {

    private final AuthUrlWhiteListProperties authUrlWhiteListProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        //白名单路径移除JWT请求头
        List<String> ignoreUrls = authUrlWhiteListProperties.getWhiteUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, uri.getPath())) {
                request = exchange.getRequest().mutate().header(AuthConstant.JWT_TOKEN_HEADER, "").build();
                exchange = exchange.mutate().request(request).build();
                return chain.filter(exchange);
            }
        }
        return chain.filter(exchange);
    }
}
