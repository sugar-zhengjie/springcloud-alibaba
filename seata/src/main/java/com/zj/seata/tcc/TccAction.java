package com.zj.seata.tcc;

public class TccAction  implements ITccTransaction{
    @Override
    public boolean prepare() {
        // 尝试执行业务操作，例如检查资源是否足够
        // 返回true表示Try阶段成功，false表示失败
        return true;
    }

    @Override
    public void commit() {
        // 确认执行业务操作，例如提交订单
        // 此方法在所有Try操作成功后被调用
    }

    @Override
    public void cancel() {
        // 取消执行业务操作，例如回滚订单
        // 此方法在Try阶段任一操作失败时被调用
    }
}
