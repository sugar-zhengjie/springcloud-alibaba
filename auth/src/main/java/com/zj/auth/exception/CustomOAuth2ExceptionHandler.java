package com.zj.auth.exception;

import com.zj.common.enums.ResultCodeEnum;
import com.zj.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/11/15 16:24
 */
@Slf4j
@RestControllerAdvice
public class CustomOAuth2ExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public Result handleInvalidTokenException(InvalidTokenException e) {
        return Result.error(ResultCodeEnum.UNAUTHORIZED);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public Result handleUsernameNotFoundException(UsernameNotFoundException e) {
        return Result.error(ResultCodeEnum.INVALID_USERNAME_PASSWORD);
    }

    @ExceptionHandler({InvalidGrantException.class})
    public Result handleInvalidGrantException(InvalidGrantException e) {
        return Result.error(ResultCodeEnum.INVALID_USERNAME_PASSWORD);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Result handleInvalidGrantException(InternalAuthenticationServiceException e) {
        Result result = Result.error(ResultCodeEnum.INVALID_USERNAME_PASSWORD);
        if (null != e) {
            String errorMsg = e.getMessage();
            if (ResultCodeEnum.INVALID_PASSWORD_CAPTCHA.getMsg().equals(errorMsg)) {
                //必须使用验证码
                result = Result.error(ResultCodeEnum.INVALID_PASSWORD_CAPTCHA);
            }
            else if (ResultCodeEnum.PASSWORD_TRY_MAX_ERROR.getMsg().equals(errorMsg)) {
                //账号被锁定
                result = Result.error(ResultCodeEnum.PASSWORD_TRY_MAX_ERROR);
            }
            else if (ResultCodeEnum.DISABLED_ACCOUNT.getMsg().equals(errorMsg)) {
                //账号被禁用
                result = Result.error(ResultCodeEnum.DISABLED_ACCOUNT);
            }
        }
        return result;
    }
}
