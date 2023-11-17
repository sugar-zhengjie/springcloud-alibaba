package com.zj.auth.service;

import javax.servlet.http.HttpServletRequest;

import com.zj.auth.client.UserFeign;
import com.zj.auth.enums.AuthEnum;
import com.zj.auth.exception.CustomOAuth2Exception;
import com.zj.common.constant.AuthConstant;
import com.zj.common.constant.BaseConstant;
import com.zj.common.constant.TokenConstant;
import com.zj.common.enums.ResultCodeEnum;
import com.zj.common.result.Result;
import com.zj.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 实现SpringSecurity获取用户信息接口
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 16:36
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 密码最大尝试次数
     */
    @Value("${system.maxTryTimes}")
    private int maxTryTimes;

    /**
     * 不需要验证码登录的最大尝试次数
     */
    @Value("${system.maxNonCaptchaTimes}")
    private int maxNonCaptchaTimes;

    @Override
    public UserDetails loadUserByUsername(String username) {

        // 获取登录类型，密码，二维码，验证码
        String authGrantType = request.getParameter(AuthConstant.GRANT_TYPE);

        // 获取客户端id
        String clientId = request.getParameter(AuthConstant.AUTH_CLIENT_ID);

        // 远程调用返回数据
        Result<Object> result;

        // 通过手机号码登录
        if (!StringUtils.isEmpty(authGrantType) && AuthEnum.SMS_CAPTCHA.code.equals(authGrantType))
        {
            String phone = request.getParameter(TokenConstant.PHONE_NUMBER);
            result = userFeign.queryUserByPhone(phone);
        }
        // 第三方登录
        else if(!StringUtils.isEmpty(authGrantType) && AuthEnum.SOCIAL.code.equals(authGrantType))
        {
            result = userFeign.queryById(Long.parseLong(username));
        }
        // 扫描二维码登录 TODO
        else if(!StringUtils.isEmpty(authGrantType) && AuthEnum.QR.code.equals(authGrantType))
        {
            result = userFeign.queryUserByAccount(username);
        }
        else
        {
            result = userFeign.queryUserByAccount(username);
        }

        // 判断返回信息
        if (null != result && result.isSuccess()) {
            User user = new User();
            BeanUtil.copyProperties(result.getData(), user, false);

            // 用户名或密码错误
            if (user.getId() == null) {
                throw new UsernameNotFoundException(ResultCodeEnum.INVALID_USERNAME.msg);
            }

            // 没有角色
            if (CollectionUtils.isEmpty(user.getRoleIdList())) {
                throw new UserDeniedAuthorizationException(ResultCodeEnum.INVALID_ROLE.msg);
            }

            // 从Redis获取账号密码错误次数
            Object lockTimes = redisTemplate.boundValueOps(AuthConstant.LOCK_ACCOUNT_PREFIX + user.getId()).get();

            // 判断账号密码输入错误几次，如果输入错误多次，则锁定账号
            // 输入错误大于配置的次数，必须选择captcha或sms_captcha
            if (null != lockTimes && (int)lockTimes > maxNonCaptchaTimes
                    && ( StringUtils.isEmpty(authGrantType) || (!StringUtils.isEmpty(authGrantType)
                    && !AuthEnum.SMS_CAPTCHA.code.equals(authGrantType) && !AuthEnum.CAPTCHA.code.equals(authGrantType)))) {
                throw new CustomOAuth2Exception(ResultCodeEnum.INVALID_PASSWORD_CAPTCHA.msg);
            }

            // 判断账号是否被锁定（账户过期，凭证过期等可在此处扩展）
            if(null != lockTimes && (int)lockTimes >= maxTryTimes){
                throw new LockedException(ResultCodeEnum.PASSWORD_TRY_MAX_ERROR.msg);
            }

            // 判断账号是否被禁用
            String userStatus = user.getStatus();
            if (String.valueOf(BaseConstant.DISABLE).equals(userStatus)) {
                throw new DisabledException(ResultCodeEnum.DISABLED_ACCOUNT.msg);
            }


            /**
             * enabled 账户是否被禁用  !String.valueOf(GitEggConstant.DISABLE).equals(gitEggUser.getStatus())
             * AccountNonExpired 账户是否过期  此框架暂时不提供账户过期功能，可根据业务需求在此处扩展
             * AccountNonLocked  账户是否被锁  密码尝试次数过多，则锁定账户
             * CredentialsNonExpired 凭证是否过期
             */
            return new UserDetails(user.getId(), user.getTenantId(), user.getOauthId(),
                    user.getNickname(), user.getRealName(), user.getOrganizationId(),
                    user.getOrganizationName(),
                    user.getOrganizationIds(), user.getOrganizationNames(), user.getRoleId(), user.getRoleIds(), user.getRoleName(), user.getRoleNames(),
                    user.getRoleIdList(), user.getRoleKeyList(), user.getResourceKeyList(),
                    user.getDataPermissionTypeList(), user.getOrganizationIdList(),
                    user.getAvatar(), user.getAccount(), user.getPassword(), !String.valueOf(BaseConstant.DISABLE).equals(user.getStatus()), true, true, true,
                    this.getPrivileges(user.getRoleKeyList(), user.getResourceUrlList()));
        } else {
            throw new UsernameNotFoundException(result.getMsg());
        }
    }

    /**
     * 设置SpringSecurity需要的role和resource
     * @param roles
     * @param resources
     * @return
     */
    private List<GrantedAuthority> getPrivileges(final Collection<String> roles, final Collection<String> resources) {
        final List<GrantedAuthority> authorities = new ArrayList<>();

        for (final String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        //不将resource权限加入token，这样会导致请求头很大
//        for (final String resource : resources) {
//            authorities.add(new SimpleGrantedAuthority(resource));
//        }

        return authorities;
    }

}
