package com.zj.config;

import com.zj.filter.RateLimitByIpGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 16:13
 */
@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 根据令牌限流
                .route(p -> p
                        .path("/ip/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(new RateLimitByIpGatewayFilter(3,1, Duration.ofSeconds(1))))
                        .uri("http://httpbin.org:80"))
                .build();
    }

}
