package com.wonnapark.wnpserver.webtoon.presentation;

import com.wonnapark.wnpserver.config.ControllerTestConfig;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.WebtoonFixtures;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GuestWebtoonControllerTest extends ControllerTestConfig {

    @Test
    @DisplayName("로그인하지 않은 사용자는 웹툰 ID로 18세 이용가가 아닌 웹툰의 상세 정보를 조회할 수 있다.")
    void findWebtoonById() throws Exception {
        // given
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        Webtoon webtoon = WebtoonFixtures.createWebtoonUnder18();
        given(guestWebtoonService.findWebtoonById(webtoon.getId())).willReturn(WebtoonDetailResponse.from(webtoon));

        // when, then
        mockMvc.perform(get("/api/v1/guest/webtoons/{webtoonId}", webtoon.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("guest-webtoon-v1-findwebtoonById",
                        resourceDetails().tag("웹툰-비회원").description("비회원 웹툰 상세 정보 불러오기"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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
}
