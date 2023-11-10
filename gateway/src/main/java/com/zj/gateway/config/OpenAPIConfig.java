package com.zj.gateway.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/9 14:15
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("网关").version("1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    @Bean
    public GroupedOpenApi gatewayOpenApi() {
        String[] paths = {"/portal/**"};
        String[] packagedToMatch = {"com.zj.gateway"};
        return GroupedOpenApi.builder().group("gateway").pathsToMatch(paths).packagesToScan(packagedToMatch)
                .build();
    }
}
