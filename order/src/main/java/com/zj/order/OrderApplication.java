package com.zj.order;

import com.alibaba.cloud.seata.rest.SeataRestTemplateInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author clinflash_zhengjie
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OrderApplication {

    @Bean
    @LoadBalanced
    public RestTemplate create(){
        return new RestTemplate();
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        // 添加Seata的RestTemplate拦截器
        restTemplate.getInterceptors().add(new SeataRestTemplateInterceptor());
        return restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class);
    }
}
