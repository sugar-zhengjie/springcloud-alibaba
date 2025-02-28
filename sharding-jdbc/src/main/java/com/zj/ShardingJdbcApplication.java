package com.zj;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//需要排除springboot中德鲁伊默认自动配置，否则会出现bean定义覆盖问题
@SpringBootApplication(exclude={DruidDataSourceAutoConfigure.class})
public class ShardingJdbcApplication {

    public static void main(String[] args){
        SpringApplication.run(ShardingJdbcApplication.class);
    }
}
