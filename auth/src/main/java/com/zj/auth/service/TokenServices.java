package com.zj.auth.service;


import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.zj.common.constant.AuthConstant;
import com.zj.common.constant.BaseConstant;
import com.zj.common.constant.TokenConstant;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

/**
 * 自定义刷新token方法，增加判断当refresh_token被加入黑名单时，提示刷新refresh_已过期
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 16:53
 */
@Slf4j
public class TokenServices extends DefaultTokenServices{

    private final RedisTemplate redisTemplate;

    public TokenServices(RedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

    @Transactional(
            noRollbackFor = {InvalidTokenException.class, InvalidGrantException.class}
    )
    @Override
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) throws AuthenticationException {

        JSONObject jsonObject = null;
        String jti = null;
        //如果refreshToken被加入到黑名单，就是执行了退出登录操作，那么拒绝访问
        try {
            JWSObject jwsObject = JWSObject.parse(refreshTokenValue);
            Payload payload = jwsObject.getPayload();
            jsonObject = (JSONObject) payload.toJSONObject();
            jti = jsonObject.getAsString(TokenConstant.JTI);
            String blackListToken = (String)redisTemplate.opsForValue().get(AuthConstant.TOKEN_BLACKLIST + jti);
            if (!StringUtils.isEmpty(blackListToken)) {
                throw new InvalidTokenException("Invalid refresh token (blackList): " + refreshTokenValue);
            }
        } catch (ParseException e) {
            log.error("获取refreshToken黑名单时发生错误：{}", e);
        }

        OAuth2AccessToken oAuth2AccessToken = super.refreshAccessToken(refreshTokenValue, tokenRequest);

        // RefreshToken不支持重复使用，如果使用一次，则加入黑名单不再允许使用，当刷新token执行完之后，即校验过RefreshToken之后，才执行存redis操作
        if (null != jsonObject && !StringUtils.isEmpty(jti)) {
            long currentTimeSeconds = System.currentTimeMillis() / BaseConstant.Number.THOUSAND;
            Long exp = Long.parseLong(jsonObject.getAsString(TokenConstant.EXP));
            if (exp - currentTimeSeconds > BaseConstant.Number.ZERO) {
                redisTemplate.opsForValue().set(AuthConstant.TOKEN_BLACKLIST + jti, jti, (exp - currentTimeSeconds), TimeUnit.SECONDS);
            }
        }

        return oAuth2AccessToken;
    }

}
