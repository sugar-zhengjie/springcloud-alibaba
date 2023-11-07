package com.zj.task;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/7 17:05
 */
@Component
public class SampleXxlJob {

    private static Logger logger = LoggerFactory.getLogger(SampleXxlJob.class);

    @XxlJob("sample1JobHandler")
    public void test1(){
        logger.info("XXL-JOB, Hello World.");
    }

    @XxlJob("sample2JobHandler")
    public ReturnT<String> test2(){
        logger.info("XXL-JOB, Hello World.");
        return ReturnT.SUCCESS;
    }
}
