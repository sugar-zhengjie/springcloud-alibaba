package com.zj.seata.saga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存服务类
 */
@Component("inventoryAction")
public class InventoryAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryAction.class);

    @Transactional(rollbackFor = Exception.class)
    public boolean reduce(String businessKey, int count) {
        LOGGER.info("reduce inventory succeed, count: " + count + ", businessKey:" + businessKey);
        if (Math.random() < 0.9999) {
            throw new RuntimeException("模拟随机异常，扣减库存失败");
        }
        return true;
    }

    public boolean compensateReduce(String businessKey) {
        LOGGER.info("compensate reduce inventory succeed, businessKey:" + businessKey);
        return true;
    }
}
