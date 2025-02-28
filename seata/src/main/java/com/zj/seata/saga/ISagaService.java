package com.zj.seata.saga;

public interface ISagaService {

    /**
     * 开始执行Saga事务
     */
    void startSaga();

    /**
     * 结束执行Saga事务
     */
    void endSaga();
}
