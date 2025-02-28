package com.zj.seata.xa;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.seata.rm.datasource.xa.DataSourceProxyXA;

@Configuration
public class XaDataSourceConfig {
    @Value("${xa.datasource.ds1}")
    private String ds1;
    @Value("${xa.datasource.ds2}")
    private String ds2;


    // seata 该特性是在 Seata 1.4.0+ 版本新增的XADataSourceProxy,如果版本符合，使用XADataSourceProxy
    @Bean
    public DataSourceProxyXA dataSource1() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(ds1);
        // 其他配置...
        return new DataSourceProxyXA(dataSource);
    }

    @Bean
    public DataSourceProxyXA dataSource2() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(ds2);
        // 其他配置...
        return new DataSourceProxyXA(dataSource);
    }
}
