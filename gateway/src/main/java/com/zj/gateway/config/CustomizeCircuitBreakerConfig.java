package com.zj.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 16:01
 */
@Configuration
public class CustomizeCircuitBreakerConfig {

    @Bean(name = "myCircuitBreaker")
    public ReactiveResilience4JCircuitBreakerFactory defaultCustomizer() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                // 滑动窗口的类型为时间窗口
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                // 时间窗口的大小为60秒
                .slidingWindowSize(10)
                // 在单位时间窗口内最少需要5次调用才能开始进行统计计算
                .minimumNumberOfCalls(5)
                // 在单位时间窗口内调用失败率达到50%后会启动断路器
                .failureRateThreshold(50)
                // 允许断路器自动由打开状态转换为半开状态
                .enableAutomaticTransitionFromOpenToHalfOpen()
                // 在半开状态下允许进行正常调用的次数
                .permittedNumberOfCallsInHalfOpenState(5)
                // 断路器打开状态转换为半开状态需要等待60秒
                .waitDurationInOpenState(Duration.ofSeconds(5))
                // 所有异常都当作失败来处理
                .recordExceptions(Throwable.class)
                .build();

        ReactiveResilience4JCircuitBreakerFactory factory = new ReactiveResilience4JCircuitBreakerFactory();
        factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofMillis(200)).build())
                .circuitBreakerConfig(circuitBreakerConfig).build());

        return factory;
    }
}
