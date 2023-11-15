package com.zj.gateway.context;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 11:12
 */
@Getter
@Setter
@ToString
public class GatewayContext {

    public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";
    /**
     * whether read request data
     */
    protected Boolean readRequestData = false;
    /**
     * whether read response data
     */
    protected Boolean readResponseData = false;
    /**
     * cache json body
     */
    protected String requestBody;
    /**
     * cache Response Body
     */
    protected Object responseBody;
    /**
     * request headers
     */
    protected HttpHeaders requestHeaders;
    /**
     * cache form data
     */
    protected MultiValueMap<String, String> formData;
    /**
     * cache all request data include:form data and query param
     */
    protected MultiValueMap<String, String> allRequestData = new LinkedMultiValueMap<>(0);

    /**
     * Gateway Extra Data
     */
    protected GatewayContextExtraData gatewayContextExtraData;

}
