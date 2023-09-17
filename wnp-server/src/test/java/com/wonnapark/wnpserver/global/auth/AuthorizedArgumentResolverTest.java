package com.wonnapark.wnpserver.global.auth;

import com.wonnapark.wnpserver.auth.exception.JwtInvalidException;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.user.infrastructure.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthorizedArgumentResolverTest {

    private class TestController {

        public void testMethod(@Authorized UserInfo userInfo, String notSupport) {
        }

    }

    @InjectMocks
    AuthorizedArgumentResolver authorizedArgumentResolver;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("@Authorized 어노테이션과 UserInfo를 타입으로 하는 파라미터만 True를 반환한다.")
    void supportsParameterTest() throws NoSuchMethodException {
        // given
        Method testMethod = TestController.class.getMethod("testMethod", UserInfo.class, String.class);
        MethodParameter targetParameter = new MethodParameter(testMethod, 0);
        MethodParameter dummyParameter = new MethodParameter(testMethod, 1);

        // when
        boolean target = authorizedArgumentResolver.supportsParameter(targetParameter);
        boolean dummy = authorizedArgumentResolver.supportsParameter(dummyParameter);

        // then
        assertThat(target).isTrue();
        assertThat(dummy).isFalse();
    }

    @Test
    @DisplayName("인증 정보에 담긴 유저 정보가 DB에 존재하면 유저 정보 객체를 반환한다.")
    void resolveArgumentTest() throws Exception {
        // given
        Authentication userAuthentication = AuthFixtures.createUserAuthentication();
        AuthenticationContextHolder.setAuthenticationHolder(userAuthentication);
        UserInfo userInfo = UserInfo.from(userAuthentication);

        given(userRepository.existsById(userInfo.userId())).willReturn(true);

        // when
        UserInfo returnedUserInfo = (UserInfo) authorizedArgumentResolver.resolveArgument(null, null, null, null);

        // then
        assertThat(returnedUserInfo).isEqualTo(userInfo);
    }

    @Test
    @DisplayName("인증 정보에 담긴 유저 정보가 DB에 존재하지 않으면 예외를 반환한다.")
    void resolveArgumentFailTest() throws Exception {
        // given
        Authentication userAuthentication = AuthFixtures.createUserAuthentication();
        AuthenticationContextHolder.setAuthenticationHolder(userAuthentication);
        UserInfo userInfo = UserInfo.from(userAuthentication);

        given(userRepository.existsById(userInfo.userId())).willReturn(false);

        // when // then
        assertThatThrownBy(() -> authorizedArgumentResolver.resolveArgument(null, null, null, null))
                .isInstanceOf(JwtInvalidException.class);
    }

}
