package com.wonnapark.wnpserver.auth.presentation;

import com.wonnapark.wnpserver.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.config.ControllerTestConfig;
import com.wonnapark.wnpserver.global.auth.AuthFixtures;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.oauth.OAuthProvider;
import com.wonnapark.wnpserver.oauth.dto.request.KakaoLoginRequest;
import com.wonnapark.wnpserver.oauth.dto.request.NaverLoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OAuthControllerTest extends ControllerTestConfig {

    @BeforeEach
    void setUp() throws Exception {
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
    }

    @ParameterizedTest
    @EnumSource(value = OAuthProvider.class)
    @DisplayName("OAuthProvider에 따라 알맞는 AuthorizeUrl을 redirectUrl로 반환한다")
    void redirectAuthorizationRequestUrlTest(OAuthProvider oAuthProvider) throws Exception {
        // given
        String redirectUrl = "authorizeUrl";
        String pathVariable = oAuthProvider.name().toLowerCase();
        given(oAuthLoginService.getAuthorizationRequestUrl(oAuthProvider)).willReturn(redirectUrl);

        // when // then
        mockMvc.perform(get("/api/v1/oauth/{oAuthProvider}", pathVariable))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(redirectUrl))
                .andDo(document("OAuth-authorizeUrlProvide",
                                resourceDetails().tag("OAuth").description("인증 Url 제공"),
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("oAuthProvider").description("소셜 타입")
                                )
                        )
                )
                .andDo(print());
    }

    @Test
    @DisplayName("카카오 로그인이 진행되고 AuthToken 정보를 반환한다.")
    void loginWithKakaoTest() throws Exception {
        // given
        AuthTokenResponse authTokenResponse = AuthFixtures.createAuthTokenResponse();
        ApiResponse<AuthTokenResponse> from = ApiResponse.from(authTokenResponse);
        given(oAuthLoginService.login(any(KakaoLoginRequest.class))).willReturn(authTokenResponse);

        // when // then
        mockMvc.perform(get("/api/v1/oauth/login/kakao")
                        .param("code", "authorizationCode"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(from)))
                .andDo(document("OAuth-kakao-login",
                                resourceDetails().tag("OAuth").description("카카오 로그인"),
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("code").description("카카오 접근 인증 코드")
                                ),
                                responseFields(
                                        fieldWithPath("data.grantType").type(JsonFieldType.STRING).description("인증 타입"),
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("data.accessTokenExpiresIn").type(JsonFieldType.NUMBER).description("액세스 토큰 유효시간"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                )
                        )
                )
                .andDo(print());
    }

    @Test
    @DisplayName("네이버 로그인이 진행되고 AuthToken 정보를 반환한다.")
    void loginWithNaverTest() throws Exception {
        // given
        AuthTokenResponse authTokenResponse = AuthFixtures.createAuthTokenResponse();
        ApiResponse<AuthTokenResponse> from = ApiResponse.from(authTokenResponse);
        given(oAuthLoginService.login(any(NaverLoginRequest.class))).willReturn(authTokenResponse);

        // when // then
        mockMvc.perform(get("/api/v1/oauth/login/naver")
                        .param("code", "authorizationCode"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(from)))
                .andDo(print())
                .andDo(document("OAuth-naver-login",
                                resourceDetails().tag("OAuth").description("네이버 로그인"),
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("code").description("네이버 접근 인증 코드")
                                ),
                                responseFields(
                                        fieldWithPath("data.grantType").type(JsonFieldType.STRING).description("인증 타입"),
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("data.accessTokenExpiresIn").type(JsonFieldType.NUMBER).description("액세스 토큰 유효시간"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                )
                        )
                );
    }

}
