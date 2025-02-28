package com.zj.seata.tcc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeataTccConfig {

    // 注册TccAction到Spring容器
    @Bean
    public ITccTransaction tccAction() {
        return new TccAction();
    }
}
