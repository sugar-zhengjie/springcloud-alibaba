package com.zj.gateway.config;

import cn.hutool.core.util.ArrayUtil;
import com.zj.common.constant.AuthConstant;
import com.zj.gateway.auth.AuthorizationManager;
import com.zj.gateway.filter.WhiteListRemoveJwtFilter;
import com.zj.gateway.handler.AuthServerAccessDeniedHandler;
import com.zj.gateway.handler.AuthServerAuthenticationEntryPoint;
import com.zj.gateway.properties.AuthUrlWhiteListProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/17 14:04
 */
@AllArgsConstructor
@Configuration
// 注解需要使用@EnableWebFluxSecurity而非@EnableWebSecurity,因为SpringCloud Gateway基于WebFlux
@EnableWebFluxSecurity
public class AuthResourceServerConfig {

    private final AuthorizationManager authorizationManager;

    private final AuthServerAccessDeniedHandler authServerAccessDeniedHandler;

    private final AuthServerAuthenticationEntryPoint authServerAuthenticationEntryPoint;

    private final AuthUrlWhiteListProperties authUrlWhiteListProperties;

    private final WhiteListRemoveJwtFilter whiteListRemoveJwtFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
        // 自定义处理JWT请求头过期或签名错误的结果
        http.oauth2ResourceServer().authenticationEntryPoint(authServerAuthenticationEntryPoint);
        // 对白名单路径，直接移除JWT请求头，不移除的话，后台会校验jwt
        http.addFilterBefore(whiteListRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        http.authorizeExchange()
                .pathMatchers(ArrayUtil.toArray(authUrlWhiteListProperties.getWhiteUrls(), String.class)).permitAll()
                .anyExchange().access(authorizationManager)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(authServerAccessDeniedHandler) // 处理未授权
                .authenticationEntryPoint(authServerAuthenticationEntryPoint) //处理未认证
                .and()
                .cors()
                .and().csrf().disable();

        return http.build();
    }

    /**
     * ServerHttpSecurity没有将jwt中authorities的负载部分当做Authentication，需要把jwt的Claim中的authorities加入
     * 解决方案：重新定义ReactiveAuthenticationManager权限管理器，默认转换器JwtGrantedAuthoritiesConverter
     */
    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
