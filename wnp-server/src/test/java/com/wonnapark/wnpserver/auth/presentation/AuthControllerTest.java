package com.wonnapark.wnpserver.auth.presentation;

import com.wonnapark.wnpserver.auth.config.TokenConstants;
import com.wonnapark.wnpserver.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.config.ControllerTestConfig;
import com.wonnapark.wnpserver.global.auth.AuthFixtures;
import com.wonnapark.wnpserver.global.auth.Authentication;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTestConfig {

    @Test
    @DisplayName("인증 정보로 액세스 토큰과 리프레시 토큰을 재발급한다.")
    void reissueAuthTokenTest() throws Exception {
        // given
        Authentication authentication = AuthFixtures.createUserAuthentication();
        AuthTokenResponse authTokenResponse = AuthFixtures.createAuthTokenResponse();
        ApiResponse<AuthTokenResponse> response = ApiResponse.from(authTokenResponse);

        willDoNothing().given(authenticationResolver).validateAccessToken(any());
        given(authenticationResolver.extractAuthentication(any())).willReturn(authentication);
        willDoNothing().given(authenticationResolver).validateRefreshToken(any());

        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        given(jwtTokenService.generateAuthToken(any(AuthTokenRequest.class))).willReturn(authTokenResponse);

        // when // then
        mockMvc.perform(get("/api/v1/auth/reissue")
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .header(TokenConstants.REFRESH_TOKEN, TOKEN))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(response)))
                .andDo(document("authToken-reissue",
                                resourceDetails().tag("토큰").description("토큰 재발급"),
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰"),
                                        headerWithName(TokenConstants.REFRESH_TOKEN).description("리프레시 토큰")
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
    @DisplayName("유저 정보와 액세스 토큰을 통해 로그아웃 처리를 진행한다.")
    void logoutTest() throws Exception {
        // given
        Authentication authentication = AuthFixtures.createUserAuthentication();
        UserInfo userInfo = UserInfo.from(authentication);

        willDoNothing().given(authenticationResolver).validateAccessToken(any());
        given(authenticationResolver.extractAuthentication(any())).willReturn(authentication);
        willDoNothing().given(authenticationResolver).validateRefreshToken(any());

        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        given(authorizedArgumentResolver.supportsParameter(any())).willReturn(true);
        given(authorizedArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);

        willDoNothing().given(oAuthLogoutService).logout(any(), any());

        // when // then
        mockMvc.perform(get("/api/v1/auth/logout")
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .header(TokenConstants.REFRESH_TOKEN, TOKEN))
                .andExpect(status().isNoContent())
                .andDo(document("auth-logout",
                                resourceDetails().tag("토큰").description("토큰 로그아웃 처리"),
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰"),
                                        headerWithName(TokenConstants.REFRESH_TOKEN).description("리프레시 토큰")
                                )
                        )
                )
                .andDo(print());
    }
}
