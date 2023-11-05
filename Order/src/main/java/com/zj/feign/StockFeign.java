package com.zj.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stock",fallback = StockFeignHandler.class)
public interface StockFeign {
    @GetMapping("/stock/reduce/{productId}")
    String reduce(@PathVariable Integer productId);
}
