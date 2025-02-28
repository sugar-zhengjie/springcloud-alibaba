package com.zj.seata.saga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 余额服务类
 * Status: 服务执行状态映射，框架定义了三个状态，SU 成功、FA 失败、UN 未知
 *
 * CompensateState: 该"状态"的补偿"状态"
 */
@Component("balanceAction")
public class BalanceAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceAction.class);

    @Transactional(rollbackFor = Exception.class)
    public boolean reduce(String businessKey, BigDecimal amount, Map<String, Object> params) {
        if(params != null && "true".equals(params.get("throwException"))){
            throw new RuntimeException("reduce balance failed");
        }
        if (Math.random() < 0.9999) {
            throw new RuntimeException("模拟随机异常，扣减账户余额失败");
        }
        LOGGER.info("reduce balance succeed, amount: " + amount + ", businessKey:" + businessKey);
        return true;
    }

    public boolean compensateReduce(String businessKey, Map<String, Object> params) {
        if(params != null && "true".equals(params.get("throwException"))){
            throw new RuntimeException("compensate reduce balance failed");
        }
        LOGGER.info("compensate reduce balance succeed, businessKey:" + businessKey);
        return true;
    }
}
