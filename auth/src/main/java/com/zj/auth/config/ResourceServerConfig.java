package com.zj.auth.config;

import cn.hutool.core.util.ArrayUtil;
import com.zj.auth.handler.LoginFailHandler;
import com.zj.auth.handler.LoginSuccessHandler;
import com.zj.auth.props.AuthUrlWhiteListProperties;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/16 9:46
 */
@Configuration
@AllArgsConstructor
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final AuthUrlWhiteListProperties authUrlWhiteListProperties;

    private final LoginSuccessHandler loginSuccessHandler;

    private final LoginFailHandler loginFailHandler;

    /**
     *
     * @param http
     * access(String) 如果给定的SpEL表达式计算结果为true，就允许访问
     * anonymous() 允许匿名用户访问
     * authenticated() 允许认证的用户进行访问
     * denyAll() 无条件拒绝所有访问
     * fullyAuthenticated() 如果用户是完整认证的话（不是通过Remember-me功能认证的），就允许访问
     * hasAuthority(String) 如果用户具备给定权限的话就允许访问
     * hasAnyAuthority(String…)如果用户具备给定权限中的某一个的话，就允许访问
     * hasRole(String) 如果用户具备给定角色(用户组)的话,就允许访问/
     * hasAnyRole(String…) 如果用户具有给定角色(用户组)中的一个的话,允许访问.
     * hasIpAddress(String 如果请求来自给定ip地址的话,就允许访问.
     * not() 对其他访问结果求反.
     * permitAll() 无条件允许访问
     * rememberMe() 如果用户是通过Remember-me功能认证的，就允许访问
     *
     */
    @Override
    @SneakyThrows
    public void configure(HttpSecurity http) {

        List<String> whiteUrlList = new ArrayList<>();
        //获取不需要鉴权的白名单配置，因为Gateway需要配置服务名，这里需要将服务名去掉
        Arrays.stream(ArrayUtil.toArray(authUrlWhiteListProperties.getWhiteUrls(), String.class)).forEach(whiteUrl -> {
            try {
                if (whiteUrl.indexOf("/", 2) != -1)
                {
                    whiteUrlList.add(whiteUrl.substring(whiteUrl.indexOf("/", 2)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        http.headers().frameOptions().disable();
        http.formLogin()
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailHandler)
                .and()
                .authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .and()
                .authorizeRequests()
                // 此处为开放权限接口，使用白名单的模式
                //.antMatchers(ArrayUtil.toArray(whiteUrlList, String.class))
                .antMatchers("/oauth/public_key")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

}