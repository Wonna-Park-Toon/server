package com.wonnapark.wnpserver.webtoon.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonnapark.wnpserver.auth.application.AuthenticationResolver;
import com.wonnapark.wnpserver.global.auth.AuthorizedArgumentResolver;
import com.wonnapark.wnpserver.global.auth.JwtAuthenticationInterceptor;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.WebtoonFixtures;
import com.wonnapark.wnpserver.webtoon.application.AdminWebtoonService;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonCreateDetailRequest;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonUpdateDetailRequest;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminWebtoonController.class)
@AutoConfigureRestDocs
class AdminWebtoonControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    AuthenticationResolver authenticationResolver;
    @MockBean
    AuthorizedArgumentResolver authorizedArgumentResolver;
    @MockBean
    JwtAuthenticationInterceptor jwtAuthenticationInterceptor;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminWebtoonService adminWebtoonService;

    @Test
    @DisplayName("새로운 웹툰을 생성하고 웹툰 상세 정보를 반환할 수 있다.")
    void createWebtoon() throws Exception {
        // given
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        WebtoonCreateDetailRequest request = WebtoonFixtures.createWebtoonCreateDetailRequest();
        Webtoon webtoon = WebtoonFixtures.createWebtoon(request);
        WebtoonDetailResponse response = WebtoonDetailResponse.from(webtoon);
        given(adminWebtoonService.createWebtoonDetail(request)).willReturn(response);

        // when, then
        mockMvc.perform(post("/api/v1/admin/webtoons")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("/api/v1/webtoons/%d", webtoon.getId())))
                .andExpect(jsonPath("data.id").value(webtoon.getId()))
                .andDo(document("admin-webtoon-v1-post-createWebtoon",
                        resourceDetails().tag("웹툰-관리자").description("웹툰 생성"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("웹툰 제목"),
                                fieldWithPath("artist").type(JsonFieldType.STRING).description("웹툰 작가"),
                                fieldWithPath("summary").type(JsonFieldType.STRING).description("웹툰 설명"),
                                fieldWithPath("genre").type(JsonFieldType.STRING).description("웹툰 장르"),
                                fieldWithPath("ageRating").type(JsonFieldType.STRING).description("웹툰 연령 등급"),
                                fieldWithPath("publishDays").type(JsonFieldType.ARRAY).description("웹툰 연재 요일")
                        ),
                        responseHeaders(headerWithName("Location").description("새로 생성된 웹툰 URI")),
                        responseFields(
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("웹툰 ID"),
                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("웹툰 제목"),
                                fieldWithPath("data.artist").type(JsonFieldType.STRING).description("웹툰 작가"),
                                fieldWithPath("data.summary").type(JsonFieldType.STRING).description("웹툰 설명"),
                                fieldWithPath("data.genre").type(JsonFieldType.STRING).description("웹툰 장르"),
                                fieldWithPath("data.thumbnail").type(JsonFieldType.STRING).description("웹툰 썸네일"),
                                fieldWithPath("data.ageRating").type(JsonFieldType.STRING).description("웹툰 연령 등급"),
                                fieldWithPath("data.publishDays").type(JsonFieldType.ARRAY).description("웹툰 연재 요일")
                        )
                ));
    }

    @Test
    @DisplayName("웹툰 상세 정보를 수정하고 수정된 결과를 반환할 수 있다.")
    void updateWebtoon() throws Exception {
        // given
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        Long webtoonId = Instancio.create(Long.class);
        WebtoonUpdateDetailRequest request = WebtoonFixtures.createWebtoonUpdateDetailRequest();
        Webtoon webtoon = WebtoonFixtures.createWebtoon(webtoonId);
        webtoon.changeDetail(
                request.title(),
                request.artist(),
                request.summary(),
                request.genre(),
                request.ageRating(),
                request.publishDays()
        );
        WebtoonDetailResponse response = WebtoonDetailResponse.from(webtoon);
        given(adminWebtoonService.updateWebtoonDetail(request, webtoonId)).willReturn(response);

        // when, then
        mockMvc.perform(patch("/api/v1/admin/webtoons/{webtoonId}", webtoonId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id").value(webtoonId))
                .andExpect(jsonPath("data.title").value(request.title()))
                .andExpect(jsonPath("data.artist").value(request.artist()))
                .andExpect(jsonPath("data.summary").value(request.summary()))
                .andExpect(jsonPath("data.genre").value(request.genre()))
                .andExpect(jsonPath("data.ageRating").value(request.ageRating()))
                .andDo(document("admin-webtoon-v1-patch-updateWebtoon",
                        resourceDetails().tag("웹툰-관리자").description("웹툰 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("웹툰 제목"),
                                fieldWithPath("artist").type(JsonFieldType.STRING).description("웹툰 작가"),
                                fieldWithPath("summary").type(JsonFieldType.STRING).description("웹툰 설명"),
                                fieldWithPath("genre").type(JsonFieldType.STRING).description("웹툰 장르"),
                                fieldWithPath("ageRating").type(JsonFieldType.STRING).description("웹툰 연령 등급"),
                                fieldWithPath("publishDays").type(JsonFieldType.ARRAY).description("웹툰 연재 요일")
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
                        )
                ));
    }

}
