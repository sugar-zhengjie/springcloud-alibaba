package com.zj.seata.saga;

import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.statelang.domain.ExecutionStatus;
import io.seata.saga.statelang.domain.StateMachineInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatusMachineService {

    @Autowired
    StateMachineEngine stateMachineEngine;

    public void start() {
        String businessKey = String.valueOf(System.currentTimeMillis());
        Map<String, Object> startParams = new HashMap<>(3);
        startParams.put("businessKey", businessKey);
        startParams.put("count", 10);
        startParams.put("amount", new BigDecimal("400"));
        StateMachineInstance inst = stateMachineEngine.startWithBusinessKey("buyGoodsOnline", null, businessKey, startParams);
        if(ExecutionStatus.SU.equals(inst.getStatus())){
            System.out.println("创建订单成功,saga transaction execute Succeed. XID: " + inst.getId());
        }else{
            System.out.println("创建订单失败 ,saga transaction execute failed. XID: " + inst.getId());
        }
    }
}
