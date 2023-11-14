package com.zj.order.service;

import com.zj.order.feign.StockFeign;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/14 14:26
 */
@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StockFeign stockFeign;

    /**
     * 此处的name与事务组id不同，用于标识全局事务的名称，主要用于追踪和调试
     */
    @GlobalTransactional(name = "zj-shop-create-order",rollbackFor = Exception.class)
    public String add(Integer productId, Integer userId) {
        // http 调用seata是无效的
        String result = restTemplate.getForObject("http://stock/stock/reduce/"+ productId,String.class);
        // RPC调用有效
        String feignResult = stockFeign.reduce(productId);
        return result + feignResult;
    }
}
