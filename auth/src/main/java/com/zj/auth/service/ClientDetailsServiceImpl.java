package com.zj.auth.service;

import lombok.SneakyThrows;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 16:13
 */
@Component
public class ClientDetailsServiceImpl extends JdbcClientDetailsService {

    public ClientDetailsServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 缓存客户端信息
     *
     * @param clientId 客户端id
     */
    @Override
    @SneakyThrows
    public ClientDetails loadClientByClientId(String clientId) {
        return super.loadClientByClientId(clientId);
    }
}
