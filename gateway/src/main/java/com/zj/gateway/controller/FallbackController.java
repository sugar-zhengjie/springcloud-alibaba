package com.zj.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 16:28
 */
@Slf4j
@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public Map<Object, Object> fallback() {
        log.info("Service is currently unavailable. Please try again later.");
        Map<Object, Object> map = new HashMap<>();
        map.put("code", 999);
        map.put("message", "server error");
        return map;
    }
}
