package com.zj.seata.tcc;

public interface ITccTransaction {

    /**
     * 尝试执行业务操作，预留必需的业务资源
     */
    boolean prepare();

    /**
     * 确认执行业务操作，正式提交Try阶段预留的业务资源
     */
    void commit();

    /**
     * 取消执行业务操作，释放Try阶段预留的业务资源
     */
    void cancel();
}
