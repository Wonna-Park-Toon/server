package com.wonnapark.wnpserver.webtoon.presentation;

import com.wonnapark.wnpserver.config.ControllerTestConfig;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.WebtoonFixtures;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonCreateDetailRequest;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonUpdateDetailRequest;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonThumbnailResponse;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;

import java.io.File;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminWebtoonControllerTest extends ControllerTestConfig {

    @BeforeEach
    void setUp() throws Exception {
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
    }

    @Test
    @DisplayName("새로운 웹툰을 생성하고 웹툰 상세 정보를 반환할 수 있다.")
    void createWebtoon() throws Exception {
        // given
        WebtoonCreateDetailRequest request = WebtoonFixtures.createWebtoonCreateDetailRequest();
        Webtoon webtoon = WebtoonFixtures.createWebtoon(request);
        WebtoonDetailResponse response = WebtoonDetailResponse.from(webtoon);
        given(adminWebtoonService.createWebtoonDetail(request)).willReturn(response);

        // when, then
        mockMvc.perform(post("/api/v1/admin/webtoons")
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
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
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        ),
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
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
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
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        ),
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

    @Test
    @DisplayName("웹툰 썸네일 사진을 수정하고 수정된 URL을 반환할 수 있다.")
    void updateWebtoonThumbnail() throws Exception {
        // given
        Long webtoonId = Instancio.create(Long.class);

        byte[] content = "test_image".getBytes();
        MockMultipartFile thumbnailMultipartFile = new MockMultipartFile("thumbnail", "thumbnail.jpg", "image/jpeg", content);
        WebtoonThumbnailResponse expectedResponse = Instancio.of(WebtoonThumbnailResponse.class)
                .generate(field(WebtoonThumbnailResponse::thumbnailUrl), gen -> gen.net().url().asString())
                .create();
        given(adminWebtoonService.updateWebtoonThumbnail(
                any(File.class), anyLong()
        )).willReturn(expectedResponse);

        // when, then
        mockMvc.perform(multipart("/api/v1/admin/webtoons/{webtoonId}/thumbnail", webtoonId)
                        .file(thumbnailMultipartFile)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod(HttpMethod.PATCH.toString());
                            return request;
                        })
                ).andExpect(status().isOk())
                .andDo(document("admin-webtoon-v1-patch-updateWebtoonThumbnail",
                        resourceDetails().tag("웹툰-관리자").description("웹툰 썸네일 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("webtoonId").description("웹툰 ID")
                        ),
                        requestParts(
                                partWithName("thumbnail").description("웹툰 썸네일 파일")
                        ),
                        responseFields(
                                fieldWithPath("data.thumbnailUrl").type(JsonFieldType.STRING).description("새로운 웹툰 썸네일")
                        )));
    }
    
    @Test
    @DisplayName("웹툰을 삭제할 수 있다.")
    void deleteWebtoon() throws Exception {
        // given
        Webtoon webtoon = WebtoonFixtures.createWebtoon();
        willDoNothing().given(adminWebtoonService).deleteWebtoon(anyLong());

        // when, then
        mockMvc.perform(delete("/api/v1/admin/webtoons/{webtoonId}", webtoon.getId())
                        .header(HttpHeaders.AUTHORIZATION, TOKEN))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("admin-webtoon-v1-delete-deleteWebtoon",
                        resourceDetails().tag("웹툰-관리자").description("웹툰 삭제"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰")
                        )
                ));
    }

}
