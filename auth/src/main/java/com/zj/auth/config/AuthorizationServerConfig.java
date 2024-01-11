package com.zj.auth.config;

import com.anji.captcha.service.CaptchaService;
import com.zj.auth.client.JustAuthFeign;
import com.zj.auth.client.SmsFeign;
import com.zj.auth.client.UserFeign;
import com.zj.auth.exception.CustomOAuth2ExceptionTranslator;
import com.zj.auth.granter.CustomTokenGranter;
import com.zj.auth.service.ClientDetailsServiceImpl;
import com.zj.auth.service.TokenServices;
import com.zj.auth.service.UserDetails;
import com.zj.common.constant.AuthConstant;
import com.zj.common.constant.TokenConstant;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.*;

/**
 * 认证服务配置
 * token过期时间是在OAuth2提供的数据库表里面配置，每种客户端配置不同的过期时间
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 16:01
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final  DataSource dataSource;

    private final  AuthenticationManager authenticationManager;

    private final  UserDetailsService userDetailsService;

    private final  UserFeign userFeign;

    private final  SmsFeign smsFeign;

    private final  JustAuthFeign justAuthFeign;

    private final  RedisTemplate redisTemplate;

    private final  CaptchaService captchaService;

    private final CustomOAuth2ExceptionTranslator customOAuth2ExceptionTranslator;

    @Value("${captcha.type}")
    private String captchaType;

    @Value("${system.secret-key}")
    private String secretKey;

    @Value("${system.secret-key-salt}")
    private String secretKeySalt;

    /**
     * 客户端信息配置
     * 正式环境请一定记得修改zj.jks配置的密码，这里默认为zzzz1111
     * TokenEnhancer 为登录用户的扩展信息，可以自己定义
     */


    @Value("${system.keyPair.keyLocation}")
    private String keyLocation;

    @Value("${system.keyPair.keyPassword}")
    private String keyPassword;

    @Value("${system.keyPair.alias}")
    private String alias;

    /*=============================================配置客户端详情服务 开始=============================================*/

    /**
     * 如果不配置，Spring Security OAuth2会使用默认的ClientDetailsService实现
     * 在OAuth2中，clientId是客户端应用程序的唯一标识符。这个标识符是在客户端应用程序在授权服务器上注册时由服务器生成的
     * 作用：安全性、跟踪和审计、限流、定制化的服务
     */
    @Override
    /**
     * Lombok库中的一个注解，主要用于简化Java中的异常处理
     * 不使用try/catch块的情况下抛出受检异常（checked exception）
     * 通过将受检异常包装成运行时异常（unchecked exception）来实现
     *
     * clients.inMemory()//内存方法是配置
     *     // .jdbc(dataSource) //jdbc方式配置 一般使用下面方式
     *     .withClient("client_id")
     *     .secret(new BCryptPasswordEncoder().encode("client_secret"))
     *     //客户端拥有的资源列表 可通过枚举或者查库
     *     .resourceIds("order","user")
     *     //该client允许的授权类型
     *     .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
     *     //允许的授权范围
     *     .scopes("all")
     *     //跳转到授权页面
     *     .autoApprove(false)
     *     //回调地址
     *     .redirectUris("http://www.baidu.com");
     *     //可通过and继续增加客户端
     *     //.and()
     */
    @SneakyThrows
    public void configure(ClientDetailsServiceConfigurer clients) {
        // 在OAuth2中，客户端详细信息包括客户端ID、客户端秘钥、授权类型、重定向URI等信息。当一个客户端尝试获取访问令牌时，OAuth2服务器需要查找并验证这个客户端的详细信息
        ClientDetailsServiceImpl jdbcClientDetailsService = new ClientDetailsServiceImpl(dataSource);
        // client-secret加密存储
        jdbcClientDetailsService.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        // 查找客户端详情的SQL语句
        // 自定义sql：select client_id, client_secret, resource_ids, scope,authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity,refresh_token_validity, additional_information, autoapprove from t_oauth_client_details where del_flag = 0 order by client_id
        // 默认sql：select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details order by client_id
        jdbcClientDetailsService.setFindClientDetailsSql(AuthConstant.FIND_CLIENT_DETAILS_SQL);
        // 选择客户端详情的SQL语句
        // 自定义sql：select client_id, client_secret, resource_ids, scope,authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity,refresh_token_validity, additional_information, autoapprove from t_oauth_client_details where del_flag = 0 and client_id = ?
        // 默认sql：select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details where client_id = ?
        // 当客户端应用请求一个访问令牌时，它发送一个请求,Authorization头部包含了客户端的凭证，其中就包括clientId。服务器会解析这个头部，提取出clientId，然后使用它去数据库中查找客户端详情
        jdbcClientDetailsService.setSelectClientDetailsSql(AuthConstant.SELECT_CLIENT_DETAILS_SQL);
        clients.withClientDetails(jdbcClientDetailsService);
    }

    /*=============================================配置客户端详情服务 结束=============================================*/


    /*===========================用来配置令牌（token）访问端点和令牌服务(tokenservices) 开始===========================*/

    /**
     * 使用非对称加密算法对token签名
     * 在Spring Security OAuth2中，JWT令牌的解析通常在JwtAccessTokenConverter类中进行。这个类有一个decode方法，它会尝试解析传入的JWT令牌。
     * 当一个请求到达服务端时，Spring Security会从请求中提取JWT令牌，然后使用JwtAccessTokenConverter的decode方法对令牌进行解析。
     * 在解析过程中，会使用之前用于签名JWT令牌的密钥对进行验证。如果令牌的签名与使用密钥对生成的签名不匹配，那么解析过程就会失败，抛出InvalidTokenException异常。
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair());
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

//    private static final String SIGN_KEY="zj";
//
//    @Bean
//    public JwtAccessTokenConverter accessTokenConvert(){
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey(SIGN_KEY);
//        return converter;
//    }

    /**
     * 从classpath下的密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(
                new ClassPathResource(keyLocation), keyPassword.toCharArray());
        return factory.getKeyPair(alias, keyPassword.toCharArray());
    }

    /**
     * 框架默认的URL链接有如下几个：
     * /oauth/authorize ： 授权端点
     * /auth/token ： 令牌端点
     * /oauth/confirm_access ： 用户确认授权提交的端点
     * /oauth/error : 授权服务错误信息端点。
     * /oauth/check_token ： 用于资源服务访问的令牌进行解析的端点
     * /oauth/token_key ： 使用Jwt令牌需要用到的提供公有密钥的端点。
     * 需要注意的是，这几个授权端点应该被Spring Security保护起来只供授
     * 权用户访问。
     */
    @Primary
    @Bean
    public DefaultTokenServices createDefaultTokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenServices tokenServices = new TokenServices(redisTemplate);
        tokenServices.setTokenStore(tokenStore());
        //支持刷新token
        tokenServices.setSupportRefreshToken(true);
        //是否重复使用RefreshToken
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        //将UserDetailsService添加到TokenServices中，以便在进行OAuth2认证时可以使用UserDetailsService来加载用户的详细信息
        addUserDetailsService(tokenServices, this.userDetailsService);
        return tokenServices;
    }

    /**
     * 检查传入的userDetailsService是否为null，如果不为null，创建一个新的PreAuthenticatedAuthenticationProvider，
     * 然后将userDetailsService包装进一个UserDetailsByNameServiceWrapper并设置到这个provider中，
     * 然后将这个ProviderManager设置到tokenServices的authenticationManager属性中。
     * 这样，当进行OAuth2认证时，就可以通过这个authenticationManager来加载用户的详细信息，包括用户的用户名、密码、权限等。
     */
    private void addUserDetailsService(DefaultTokenServices tokenServices, UserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
            tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
        }
    }

    /**
     * JWT内容增强
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> map = new HashMap<>(2);
            UserDetails user = (UserDetails) authentication.getUserAuthentication().getPrincipal();
            map.put(TokenConstant.TENANT_ID, user.getTenantId());
            map.put(TokenConstant.OAUTH_ID, user.getOauthId());
            map.put(TokenConstant.USER_ID, user.getId());
            map.put(TokenConstant.ORGANIZATION_ID, user.getOrganizationId());
            map.put(TokenConstant.ORGANIZATION_NAME, user.getOrganizationName());
            map.put(TokenConstant.ORGANIZATION_IDS, user.getOrganizationIds());
            map.put(TokenConstant.ORGANIZATION_NAMES, user.getOrganizationNames());
            map.put(TokenConstant.ROLE_ID, user.getRoleId());
            map.put(TokenConstant.ROLE_NAME, user.getRoleName());
            map.put(TokenConstant.ROLE_IDS, user.getRoleIds());
            map.put(TokenConstant.ROLE_NAMES, user.getRoleNames());
            map.put(TokenConstant.ACCOUNT, user.getAccount());
            map.put(TokenConstant.REAL_NAME, user.getRealName());
            map.put(TokenConstant.NICK_NAME, user.getNickname());
            map.put(TokenConstant.ROLE_ID_LIST, user.getRoleIdList());
            //建议不把权限菜单放到jwt里面,当菜单太多时，会导致jwt长度不可控
            map.put(TokenConstant.ROLE_KEY_LIST, user.getRoleKeyList());
            map.put(TokenConstant.ORGANIZATION_ID_LIST, user.getOrganizationIdList());
            map.put(TokenConstant.AVATAR, user.getAvatar());
            map.put(TokenConstant.DATA_PERMISSION_TYPE_LIST, user.getDataPermissionTypeList());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
            return accessToken;
        };
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        // 增强令牌
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        // 增强内容
        tokenEnhancers.add(tokenEnhancer());
        // 增强加密
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        // 获取自定义tokenGranter
        TokenGranter tokenGranter = CustomTokenGranter.getTokenGranter(authenticationManager, endpoints, redisTemplate,
                userFeign, smsFeign, justAuthFeign, captchaService, userDetailsService, captchaType, secretKey, secretKeySalt);

        endpoints.authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .userDetailsService(userDetailsService)
                .tokenGranter(tokenGranter)
                .tokenServices(createDefaultTokenServices(endpoints))
                /**
                 *
                 * refresh_token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                 * 1.重复使用：access_token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                 * 2.非重复使用：access_token过期刷新时， refresh_token过期时间延续，在refresh_token有效期内刷新而无需失效再次登录
                 */
                .reuseRefreshTokens(false)
                //自定义异常返回消息
                .exceptionTranslator(customOAuth2ExceptionTranslator);
    }


    /*===========================用来配置令牌（token）访问端点和令牌服务(tokenservices) 结束===========================*/

    /*==========================================来配置令牌端点的安全约束 开始==========================================*/
    /**
     * 允许表单认证
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients()
                // oauth/token_key公开
                .tokenKeyAccess("permitAll()")
                // oauth/check_token公开
                //.checkTokenAccess("permitAll()")
                // 进行了身份验证的用户才能检查OAuth2令牌的有效性
                .checkTokenAccess("isAuthenticated()");
    }
    /*==========================================来配置令牌端点的安全约束 结束==========================================*/


}
