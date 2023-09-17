package com.wonnapark.wnpserver.global.auth;

import com.wonnapark.wnpserver.auth.exception.JwtInvalidException;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.global.response.ErrorCode;
import com.wonnapark.wnpserver.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthorizedArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Authorized.class) != null
                && parameter.getParameterType().equals(UserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = AuthenticationContextHolder.getAuthentication();
        UserInfo userInfo = UserInfo.from(authentication);
        if (userRepository.existsById(userInfo.userId()))
            return userInfo;
        throw new JwtInvalidException(ErrorCode.UNSUPPORTED_TOKEN);
    }

}
