package com.wonnapark.wnpserver.webtoon.presentation;

import com.wonnapark.wnpserver.config.ControllerTestConfig;
import com.wonnapark.wnpserver.global.auth.AuthFixtures;
import com.wonnapark.wnpserver.global.auth.Authentication;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.WebtoonFixtures;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonSimpleResponse;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonsOnPublishDayResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DefaultWebtoonControllerTest extends ControllerTestConfig {

    @DisplayName("로그인한 모든 회원은 웹툰 ID로 18세이용가가 아닌 웹툰의 상세 정보를 조회할 수 있다.")
    @Test
    void findWebtoonById() throws Exception {
        // given
        Authentication authentication = AuthFixtures.createUserAuthentication();
        UserInfo userInfo = UserInfo.from(authentication);

        given(authorizedArgumentResolver.supportsParameter(any())).willReturn(true);
        given(authorizedArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        Webtoon webtoon = WebtoonFixtures.createWebtoonUnder18();
        given(userWebtoonService.findWebtoonById(webtoon.getId(), userInfo)).willReturn(WebtoonDetailResponse.from(webtoon));
        // when, then
        mockMvc.perform(get("/api/v1/webtoons/{webtoonId}", webtoon.getId())
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-webtoon-v1-findWebtoonsById",
                        resourceDetails().tag("웹툰-회원")
                                .description("회원 웹툰 상세 정보 불러오기"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("webtoonId").description("웹툰 ID")
                        ),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("웹툰 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("웹툰 제목"),
                                fieldWithPath("data.artist").type(JsonFieldType.STRING).description("웹툰 작가"),
                                fieldWithPath("data.summary").type(JsonFieldType.STRING).description("웹툰 설명"),
                                fieldWithPath("data.genre").type(JsonFieldType.STRING).description("웹툰 장르"),
                                fieldWithPath("data.thumbnail").type(JsonFieldType.STRING).description("웹툰 썸네일"),
                                fieldWithPath("data.ageRating").type(JsonFieldType.STRING).description("웹툰 연령 등급"),
                                fieldWithPath("data.publishDays").type(JsonFieldType.ARRAY).description("웹툰 연재 요일")
                        )));
    }

    @ParameterizedTest
    @EnumSource(DayOfWeek.class)
    @DisplayName("연재 요일로 해당 연재 요일의 웹툰 목록을 조회순으로 조회할 수 있다.")
    void findWebtoonsByPublishDayOrdrByView(DayOfWeek publishDay) throws Exception {
        // given
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        List<Webtoon> webtoons = WebtoonFixtures.createWebtoonsOnPublishDay(publishDay);
        given(defaultWebtoonService.findWebtoonsByPublishDayOrderByViewCount(publishDay))
                .willReturn(webtoons.stream()
                        .map(WebtoonSimpleResponse::from)
                        .toList());

        // TODO: 2023-09-16 조회순 정렬 확인 로직 추가 필요
        // when, then
        mockMvc.perform(get("/api/v1/webtoons/list")
                        .queryParam("publishDay", publishDay.name())
                        .queryParam("orderOption", OrderOption.VIEW_COUNT.name()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("default-webtoon-v1-findWebtoonsByPublishDayOrderByView",
                        resourceDetails().tag("웹툰-기본")
                                .description("특정 요일의 웹툰 정보 조회순으로 불러오기"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("publishDay").description("연재 요일"),
                                parameterWithName("orderOption").description("정렬 조건")
                        ),
                        responseFields(
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("웹툰 ID"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("웹툰 제목"),
                                fieldWithPath("data[].artist").type(JsonFieldType.STRING).description("웹툰 작가"),
                                fieldWithPath("data[].thumbnail").type(JsonFieldType.STRING).description("웹툰 썸네일"),
                                fieldWithPath("data[].ageRating").type(JsonFieldType.STRING).description("웹툰 연령 등급")
                        )));

    }

    @ParameterizedTest
    @EnumSource(value = OrderOption.class, mode = EnumSource.Mode.EXCLUDE, names = {"UPDATE"})
    @DisplayName("랭킹 옵션에 맞게 모든 연재 요일의 웹툰 목록을 조회할 수 있다.")
    void findAllWebtoons(OrderOption orderOption) throws Exception {
        // given
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        List<Webtoon> webtoons = WebtoonFixtures.createWebtoons();
        List<WebtoonsOnPublishDayResponse> webtoonsOnPublishDay = new ArrayList<>();
        for (DayOfWeek publishDay : DayOfWeek.values()) {
            List<WebtoonSimpleResponse> webtoonSimpleResponses = webtoons.stream()
                    .filter(webtoon -> webtoon.getPublishDays().contains(publishDay))
                    .map(WebtoonSimpleResponse::from)
                    .toList();

            webtoonsOnPublishDay.add(WebtoonsOnPublishDayResponse.of(publishDay, webtoonSimpleResponses));
        }

        if (orderOption.equals(OrderOption.VIEW_COUNT)) {
            given(defaultWebtoonService.findAllWebtoonsOrderByViewCount())
                    .willReturn(webtoonsOnPublishDay);
        } else if (orderOption.equals(OrderOption.POPULARITY)) {
            given(defaultWebtoonService.findAllWebtoonsOrderByPopularity())
                    .willReturn(webtoonsOnPublishDay);
        }

        // when, then
        mockMvc.perform(get("/api/v1/webtoons")
                        .queryParam("orderOption", orderOption.name()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("default-webtoon-v1-findAllWebtoons",
                        resourceDetails().tag("웹툰-기본")
                                .description("모든 요일의 웹툰 정보 랭킹 조건에 따라 불러오기"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("orderOption").description("랭킹 조건")
                        ),
                        responseFields(
                                fieldWithPath("data[].publishDay").type(JsonFieldType.STRING).description("연재 요일"),
                                fieldWithPath("data[].webtoons").type(JsonFieldType.ARRAY).description("해당 연재 요일의 웹툰 목록"),
                                fieldWithPath("data[].webtoons[].id").type(JsonFieldType.NUMBER).description("웹툰 ID"),
                                fieldWithPath("data[].webtoons[].title").type(JsonFieldType.STRING).description("웹툰 제목"),
                                fieldWithPath("data[].webtoons[].artist").type(JsonFieldType.STRING).description("웹툰 작가"),
                                fieldWithPath("data[].webtoons[].thumbnail").type(JsonFieldType.STRING).description("웹툰 썸네일"),
                                fieldWithPath("data[].webtoons[].ageRating").type(JsonFieldType.STRING).description("웹툰 연령 등급")
                        )));

    }

}
