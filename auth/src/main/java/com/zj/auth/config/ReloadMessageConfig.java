package com.zj.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * oauth2提示信息默认改为中文
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 17:12
 */
@Configuration
public class ReloadMessageConfig {
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // 指定类路径的时候，不需要添加文件后缀[.properties]
        messageSource.setBasename("classpath:org/springframework/security/messages_zh_CN");
        return messageSource;
    }
}
