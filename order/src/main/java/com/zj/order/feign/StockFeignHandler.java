package com.zj.order.feign;


import org.springframework.stereotype.Component;

@Component
public class StockFeignHandler implements StockFeign{
    @Override
    public String reduce(Integer productId) {
        String fallback = "当前查询人数过多，请稍后重试！";
        return fallback;
    }
}
