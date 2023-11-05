package com.zj.controller;

import com.zj.feign.StockFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StockFeign stockFeign;

    @RequestMapping("/add")
    public String createOrder(Integer productId,Integer userId){
//        String productName = restTemplate.getForObject("http://127.0.0.1:9000/product/"+ productId,String.class);
//        String userName = restTemplate.getForObject("http://127.0.0.1:11000/user/"+ userId,String.class);
        String result = restTemplate.getForObject("http://stock/stock/reduce/"+ productId,String.class);
        String feignResult = stockFeign.reduce(productId);
        return result + feignResult;
//        String cartResult = restTemplate.getForObject("http://127.0.0.1:12000/shopcart/remove/"+ userId+"/"+productId,String.class);
//        return "商品Id:" + productName + "用户Id：" + userName + "库存结果：" + result + "购物车结果：" + cartResult;
    }
}
