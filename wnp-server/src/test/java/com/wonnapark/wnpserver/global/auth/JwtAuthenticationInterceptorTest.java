package com.wonnapark.wnpserver.global.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.method.HandlerMethod;

import javax.naming.AuthenticationException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtAuthenticationInterceptorTest {

    private class TestController {

        @Admin
        public void adminMethod() {
        }

    }

    JwtAuthenticationInterceptor jwtAuthenticationInterceptor = new JwtAuthenticationInterceptor();

    @Test
    @DisplayName("@Admin을 적용한 메서드는 요청자의 권한이 Admin인 경우만 True를 반환한다.")
    void adminMethodWithAdminRoleTest() throws Exception {
        // given
        Method adminMethod = TestController.class.getMethod("adminMethod");
        TestController controller = new TestController();
        Object handlerMethod = new HandlerMethod(controller, adminMethod);

        Authentication adminAuthentication = AuthFixtures.createAdminAuthentication();
        AuthenticationContextHolder.setAuthenticationHolder(adminAuthentication);

        // when
        boolean result = jwtAuthenticationInterceptor.preHandle(null, null, handlerMethod);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("@Admin을 적용한 메서드에 일반 유저가 요청을 보내면 인가 예외를 반환한다.")
    void adminMethodWithUserRoleTest() throws Exception {
        // given
        Method adminMethod = TestController.class.getMethod("adminMethod");
        TestController controller = new TestController();
        Object handlerMethod = new HandlerMethod(controller, adminMethod);

        Authentication userAuthentication = AuthFixtures.createUserAuthentication();
        AuthenticationContextHolder.setAuthenticationHolder(userAuthentication);

        // when
        assertThatThrownBy(() -> jwtAuthenticationInterceptor.preHandle(null, null, handlerMethod))
                .isInstanceOf(AuthenticationException.class);
    }

}
