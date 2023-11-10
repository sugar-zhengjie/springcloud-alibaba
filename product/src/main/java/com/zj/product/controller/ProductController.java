package com.zj.product.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @RequestMapping("/{productId}")
    public String getProduct(@PathVariable Integer productId){
        return "iphone 14";
    }
}
