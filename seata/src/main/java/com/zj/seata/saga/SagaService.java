package com.zj.seata.saga;

import io.seata.spring.annotation.GlobalTransactional;

public class SagaService implements ISagaService{

    @GlobalTransactional(name = "saga-transaction", rollbackFor = Exception.class)
    @Override
    public void startSaga() {
        // 执行订单服务
        // orderService.createOrder();
        // 执行库存服务
        // storageService.deductInventory();
        // 执行账户服务
        // accountService.deductBalance();
    }

    @Override
    public void endSaga() {
        // 这里可以放置Saga事务结束时需要执行的逻辑
        // 一般执行每个服务发生错误的补偿机制方法，比如订单服务发生异常，则执行库存回滚，账户回滚等，方法在各自service层中维护
    }
}
