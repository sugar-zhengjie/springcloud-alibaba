package com.zj.seata.tcc;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    private final TccAction tccAction = new TccAction();


    // 在业务方法上使用@GlobalTransactional注解来启动全局事务
    @GlobalTransactional
    public void executeBusiness() {
        business();
    }

    public void business() {
       // 执行tcc事务
        try {
            if (!tccAction.prepare()) {
                // Try阶段成功，执行Confirm
                tccAction.commit();
            } else {
                // Try阶段失败，执行Cancel
                tccAction.cancel();
            }
        } catch (Exception e) {
            // 异常处理，可能需要调用Cancel
            tccAction.cancel();
            throw e;
        }
    }
}
