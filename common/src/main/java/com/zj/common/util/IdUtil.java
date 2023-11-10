package com.zj.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/10 14:16
 */
@Component
public class IdUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    final int DEFAULT_LENGTH = 4;

    String appPrefix = "app";

    String bizPrefix = "test";

    /**
     * 获取通用自增ID
     */
    public String getIncrId() {
        //** 自增 *//*
        String seq = getSequence(appPrefix+bizPrefix);
        return appPrefix + bizPrefix + getCurrentDate() + seq;
    }

    /**
     * SnowflakeGenerator
     */
    public static Long getId() {
        return com.baomidou.mybatisplus.core.toolkit.IdWorker.getId();
    }


    /**
     * @param redisKey (前缀和key相同)
     */
    public String generate(String redisKey) {
        //** 自增 *//*
        String seq = getSequence(redisKey);
        return redisKey + "-" + getCurrentDate() + seq;
    }

    /**
     * @param redisKey (前缀和key相同,生成8位数字流水码)
     */
    public String generateOnlyNumber(String redisKey) {
        //** 自增 *//*
        String seq = getSequence(redisKey);
        return redisKey + seq;
    }

    /**
     * @param prefix   (前缀)
     * @param redisKey (redis key)
     * @return
     */
    public String generate(String prefix, String data, String redisKey) {
        //** 自增 *//*
        String seq = getSequence(redisKey);
        return prefix + data + getCurrentDate() + seq;
    }

    /**
     * 填充000
     *
     * @param redisKey
     * @return
     */
    public String getSequence(String redisKey) {
        long seq = getId(redisKey);
        String str = String.valueOf(seq);
        int len = str.length();
        // 取决于业务规模
        if (len >= DEFAULT_LENGTH) {
            return str;
        }
        int rest = DEFAULT_LENGTH - len;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rest; i++) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }


    /**
     * 得到系统当前日期
     * "yyyyMMdd"
     */
    public String getCurrentDate() {
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMdd");
        return tempDate.format(new Date());
    }

    /**
     * @param key
     * @return
     * @Title: generate
     * @Description: Atomically increments by one the current value.
     */
    public long getId(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.incrementAndGet();
    }

}

