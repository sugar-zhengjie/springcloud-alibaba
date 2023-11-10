package com.zj.shopCart.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopcart")
public class ShopCartController {

    @RequestMapping("/remove/{userId}/{productId}")
    public String remove(@PathVariable String productId, @PathVariable String userId){
        return "移除购物车成功";
    }
}
