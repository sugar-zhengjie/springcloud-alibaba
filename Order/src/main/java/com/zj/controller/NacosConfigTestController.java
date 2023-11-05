package com.zj.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/test")
public class NacosConfigTestController {
    @Value("${owner}")
    private String owner;

    @GetMapping("/config")
    public String test(){
        return owner;
    }
}
