package com.zj.seata.saga;

import io.seata.saga.engine.config.DbStateMachineConfig;
import io.seata.saga.engine.impl.ProcessCtrlStateMachineEngine;
import io.seata.saga.rm.StateMachineEngineHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class StateMachineEngineConfig {

    @Autowired
    DataSource dataSource;


    @Bean
    public DbStateMachineConfig dbStateMachineConfig(){
        DbStateMachineConfig stateMachineConfig = new DbStateMachineConfig();
        stateMachineConfig.setDataSource(dataSource);
        // 使用 String 数组配置 JSON 文件路径（支持 classpath: 前缀）
        String[] resourcePaths = new String[] {
                "classpath:statelang/reduce.json",
        };
        stateMachineConfig.setResources(resourcePaths);
        stateMachineConfig.setEnableAsync(true);
        stateMachineConfig.setThreadPoolExecutor(threadExecutor());
        return stateMachineConfig;
    }

    @Bean
    public ProcessCtrlStateMachineEngine stateMachineEngine(){
        ProcessCtrlStateMachineEngine processCtrlStateMachineEngine = new ProcessCtrlStateMachineEngine();
        processCtrlStateMachineEngine.setStateMachineConfig(dbStateMachineConfig());
        return processCtrlStateMachineEngine;
    }


    @Bean
    public StateMachineEngineHolder stateMachineEngineHolder(){
        StateMachineEngineHolder engineHolder = new StateMachineEngineHolder();
        engineHolder.setStateMachineEngine(stateMachineEngine());
        return engineHolder;
    }


    @Bean
    public ThreadPoolExecutor threadExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(1);
        //配置最大线程数
        executor.setMaxPoolSize(20);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("SAGA_ASYNC_EXE_");

        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }

}
