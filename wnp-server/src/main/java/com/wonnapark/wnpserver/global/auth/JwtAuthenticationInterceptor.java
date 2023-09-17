package com.wonnapark.wnpserver.global.auth;

import com.wonnapark.wnpserver.user.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.naming.AuthenticationException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod))
            return true;
        
        if (!isAdminMethod(handlerMethod))
            return true;

        Authentication authentication = AuthenticationContextHolder.getAuthentication();
        Role userRole = authentication.role();
        Role handlerRole = getMethodRole(handlerMethod);

        if (!userRole.equals(handlerRole))
            throw new AuthenticationException();
        return true;
    }

    private boolean isAdminMethod(HandlerMethod handlerMethod) {
        return handlerMethod.getMethodAnnotation(Admin.class) != null;
    }

    private Role getMethodRole(HandlerMethod handlerMethod) {
        return Objects.requireNonNull(handlerMethod.getMethodAnnotation(Admin.class)).role();
    }

}
