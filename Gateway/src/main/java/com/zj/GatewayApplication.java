package com.zj;

import com.zj.handler.FallbackHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 13:52
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public FallbackHandler fallbackHandler() {
        return new FallbackHandler();
    }
}
