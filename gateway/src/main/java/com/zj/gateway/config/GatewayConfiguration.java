package com.zj.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Configuration
public class GatewayConfiguration {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /**
     * 限流的异常处理器
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    /***
     * Sentinel路由处理核心过滤器
     */
    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    public void doInit() {
        // 自定义 api 分组
        initCustomizedApis();
        // 初始化网关流控规则
        initGatewayRules();
    }

    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();
        ApiDefinition api1 = new ApiDefinition("customer_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/order/**")
                            /**
                             * 匹配策略：
                             * URL_MATCH_STRATEGY_EXACT：url精确匹配
                             * URL_MATCH_STRATEGY_PREFIX：url前缀匹配
                             * URL_MATCH_STRATEGY_REGEX：url正则匹配
                             */
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});
        definitions.add(api1);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();

        rules.add(new GatewayFlowRule("user") // 资源名称，可以是网关中的 routeid或者用户自定义的 API分组名称
                .setCount(2) // 限流阈值
                .setIntervalSec(10) // 统计时间窗口默认1s
                .setGrade(RuleConstant.FLOW_GRADE_QPS) // 限流模式
                /**
                 * 限流行为:
                 * CONTROL_BEHAVIOR_RATE_LIMITER 匀速排队
                 * CONTROL_BEHAVIOR_DEFAULT 快速失败(默认)
                 * CONTROL_BEHAVIOR_WARM_UP：
                 * CONTROL_BEHAVIOR_WARM_UP_RATE_LIMITER：
                 */
                .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
                //匀速排队模式下的最长排队时间，单位是毫秒，仅在匀速排队模式下生效
                .setMaxQueueingTimeoutMs(1000)
                /**
                 * 热点参数限流配置
                 * 若不设置,该网关规则将会被转换成普通流控规则；否则会转换成热点规则
                 */
                .setParamItem(new GatewayParamFlowItem()
                        /**
                         * 从请求中提取参数的策略:
                         * PARAM_PARSE_STRATEGY_CLIENT_IP
                         * PARAM_PARSE_STRATEGY_HOST
                         * PARAM_PARSE_STRATEGY_HEADER
                         * PARAM_PARSE_STRATEGY_URL_PARAM
                         */
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
                        /**
                         * 若提取策略选择 Header 模式或 URL 参数模式，
                         * 则需要指定对应的 header 名称或 URL 参数名称。
                         */
                        .setFieldName("token")
                        /**
                         * 参数的匹配策略：
                         * PARAM_MATCH_STRATEGY_EXACT
                         * PARAM_MATCH_STRATEGY_PREFIX
                         * PARAM_MATCH_STRATEGY_REGEX
                         * PARAM_MATCH_STRATEGY_CONTAINS
                         */
                        .setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_EXACT)
                        //参数值的匹配模式，只有匹配该模式的请求属性值会纳入统计和流控
                        .setPattern("123456") // token=123456 10s内qps达到2次会被限流
                )
        );

        rules.add(new GatewayFlowRule("customer_api")
                /**
                 * 规则是针对 API Gateway 的 route（RESOURCE_MODE_ROUTE_ID）
                 * 还是用户在 Sentinel 中定义的 API 分组（RESOURCE_MODE_CUSTOM_API_NAME），默认是 route。
                 */
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(2)
                .setIntervalSec(1)
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
        );
        GatewayRuleManager.loadRules(rules);
    }


}