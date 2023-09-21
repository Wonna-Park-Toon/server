package com.wonnapark.wnpserver.episode.presentation;

import com.wonnapark.wnpserver.config.ControllerTestConfig;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeUrlsUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeImagesUploadResponse;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
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
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminEpisodeControllerTest extends ControllerTestConfig {

    @BeforeEach
    void setup() throws Exception {
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
    }

    @Test
    @DisplayName("새로운 에피소드를 생성하고 Location 헤더와 에피소드 ID를 반환 할 수 있다.")
    void episodeCreate() throws Exception {
        // given
        Long webtoonId = 1L;
        EpisodeCreationRequest episodeCreationRequest = episodeCreationRequest();
        Long createdEpisodeId = Instancio.create(Long.class);
        given(episodeManageUseCase.createEpisode(webtoonId, episodeCreationRequest)).willReturn(createdEpisodeId);
        // when // then
        mockMvc.perform(post("/api/v1/admin/episode")
                        .header(AUTHORIZATION, TOKEN)
                        .param("webtoonId", String.valueOf(webtoonId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeCreationRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("/api/v1/common/episode/detail/%d", createdEpisodeId)))
                .andDo(document("admin-episode-v1-post-createEpisode",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 생성"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰")
                        ),
                        queryParameters(
                                parameterWithName("webtoonId").description("웹툰 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(STRING).description("에피소드 제목"),
                                fieldWithPath("releaseDateTime").type(STRING).description("에피소드 공개일"),
                                fieldWithPath("thumbnail").type(STRING).description("에피소드 썸네일 URL"),
                                fieldWithPath("artistComment").type(STRING).description("작가의 말"),
                                fieldWithPath("episodeUrlCreationRequests").type(ARRAY).description("에피소드 URL 리스트"),
                                fieldWithPath("episodeUrlCreationRequests[].episodeUrl").type(STRING).description("에피소드 URL")
                        ),
                        responseHeaders(headerWithName(LOCATION).description("새로 생성된 리소스의 LOCATION")),
                        responseFields(
                                fieldWithPath("data.id").type(NUMBER).description("에피소드 제목")
                        )
                ));
    }

    @Test
    @DisplayName("에피소드 제목을 업데이트 할 수 있다.")
    void updateEpisodeTitle() throws Exception {
        Long episodeId = Instancio.create(Long.class);
        EpisodeTitleUpdateRequest request = episodeTitleUpdateRequest();

        mockMvc.perform(patch("/api/v1/admin/episode/{id}/title", episodeId)
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-patch-updateEpisodeTitle",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 제목 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(STRING).description("새로운 에피소드 제목")
                        )));
    }

    @Test
    @DisplayName("에피소드 작가의 말을 업데이트 할 수 있다.")
    void updateEpisodeArtistComment() throws Exception {
        Long episodeId = Instancio.create(Long.class);
        EpisodeArtistCommentUpdateRequest request = episodeArtistCommentUpdateRequest();

        mockMvc.perform(patch("/api/v1/admin/episode/{id}/artist-comment", episodeId)
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-patch-updateEpisodeArtistComment",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 작가의 말 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        ),
                        requestFields(
                                fieldWithPath("artistComment").type(STRING).description("새로운 에피소드 작가의 말")
                        )));
    }

    @Test
    @DisplayName("에피소드 썸네일을 업데이트 할 수 있다.")
    void updateEpisodeThumbnail() throws Exception {
        Long episodeId = Instancio.create(Long.class);
        EpisodeThumbnailUpdateRequest request = episodeThumbnailUpdateRequest();

        mockMvc.perform(patch("/api/v1/admin/episode/{id}/thumbnail", episodeId)
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-patch-updateEpisodeThumbnail",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 썸네일 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        ),
                        requestFields(
                                fieldWithPath("thumbnail").type(STRING).description("새로운 에피소드 썸네일")
                        )));
    }

    @Test
    @DisplayName("에피소드 공개일을 업데이트 할 수 있다.")
    void updateEpisodeReleaseDateTime() throws Exception {
        Long episodeId = Instancio.create(Long.class);
        EpisodeReleaseDateTimeUpdateRequest request = episodeReleaseDateTimeUpdateRequest();

        mockMvc.perform(patch("/api/v1/admin/episode/{id}/release-datetime", episodeId)
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-patch-updateEpisodeReleaseDateTime",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 공개일 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        ),
                        requestFields(
                                fieldWithPath("releaseDateTime").type(STRING).description("새로운 에피소드 공개일")
                        )));
    }

    @Test
    @DisplayName("에피소드 URL 전부를 업데이트 할 수 있다.")
    void updateEpisodeUrls() throws Exception {
        Long episodeId = Instancio.create(Long.class);
        EpisodeUrlsUpdateRequest request = episodeUrlsUpdateRequest();

        mockMvc.perform(patch("/api/v1/admin/episode/{id}/image-urls", episodeId)
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-patch-updateEpisodeUrls",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 URL 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        ),
                        requestFields(
                                fieldWithPath("urls[]").description("에피소드 이미지 URL 목록")
                        )));
    }

    @Test
    @DisplayName("에피소드를 삭제할 수 있다.")
    void deleteEpisode() throws Exception {
        Long episodeId = Instancio.create(Long.class);

        mockMvc.perform(delete("/api/v1/admin/episode/{id}", episodeId)
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-delete-deleteEpisode",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 삭제"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        )
                ));
    }

    @Test
    @DisplayName("에피소드 이미지들을 업로드할 수 있다.")
    void createEpisodeImages() throws Exception {
        // given
        Long webtoonId = Instancio.create(Long.class);
        MockMultipartFile thumbnail = new MockMultipartFile(
                "thumbnail", "thumbnail.jpg", IMAGE_JPEG_VALUE, "THUMBNAIL".getBytes()
        );
        List<MockMultipartFile> episodeImages = List.of(
                new MockMultipartFile(
                        "episodeImage1", "episodeImage1.jpg", IMAGE_JPEG_VALUE, "episodeImage1".getBytes()
                ),
                new MockMultipartFile(
                        "episodeImage2", "episodeImage2.jpg", IMAGE_JPEG_VALUE, "episodeImage2".getBytes()
                ),
                new MockMultipartFile(
                        "episodeImage3", "episodeImage3.jpg", IMAGE_JPEG_VALUE, "episodeImage3".getBytes()
                )
        );

        EpisodeImagesUploadResponse episodeImagesUploadResponse = new EpisodeImagesUploadResponse(
                "www.s3.com/thumbnail.jpg",
                List.of(
                        "www.s3.com/episodeImage1.jpg",
                        "www.s3.com/episodeImage2.jpg",
                        "www.s3.com/episodeImage3.jpg"
                )
        );

        given(episodeImageService.uploadEpisodeMedia(any(), any(), any())).willReturn(episodeImagesUploadResponse);

        // when // then
        mockMvc.perform(multipart("/api/v1/admin/episode/images")
                        .file("thumbnail", thumbnail.getBytes())
                        .file("episodeImages", episodeImages.get(0).getBytes())
                        .file("episodeImages", episodeImages.get(1).getBytes())
                        .file("episodeImages", episodeImages.get(2).getBytes())
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .queryParam("webtoonId", String.valueOf(webtoonId))
                )
                .andExpect(status().isCreated())
                .andDo(document("admin-episode-v1-create-createEpisodeImages",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 이미지 업로드"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("엑세스 토큰")
                        ),
                        queryParameters(
                                parameterWithName("webtoonId").description("웹툰 ID")
                        ),
                        requestParts(
                                partWithName("thumbnail").description("썸네일 MULTIPART FILE"),
                                partWithName("episodeImages").description("에피소드 MULTIPART FILE")
                        ),
                        responseHeaders(headerWithName(LOCATION).description("에피소드 생성 LOCATION")),
                        responseFields(
                                fieldWithPath("data.thumbnailUrl").description("썸네일 URL"),
                                fieldWithPath("data.urls[]").description("에피소드 이미지 URLS")
                        )
                ));
    }

    private EpisodeCreationRequest episodeCreationRequest() {
        return Instancio.of(EpisodeCreationRequest.class)
                .generate(field(EpisodeCreationRequest::title), gen -> gen.string().length(1, 35))
                .generate(field(EpisodeCreationRequest::thumbnail), gen -> gen.string().length(1, 255))
                .generate(field(EpisodeCreationRequest::artistComment), gen -> gen.string().length(1, 100))
                .create();
    }

    private EpisodeTitleUpdateRequest episodeTitleUpdateRequest() {
        return Instancio.create(EpisodeTitleUpdateRequest.class);
    }

    private EpisodeThumbnailUpdateRequest episodeThumbnailUpdateRequest() {
        return Instancio.create(EpisodeThumbnailUpdateRequest.class);
    }

    private EpisodeArtistCommentUpdateRequest episodeArtistCommentUpdateRequest() {
        return Instancio.create(EpisodeArtistCommentUpdateRequest.class);
    }

    private EpisodeReleaseDateTimeUpdateRequest episodeReleaseDateTimeUpdateRequest() {
        return Instancio.create(EpisodeReleaseDateTimeUpdateRequest.class);
    }

    private EpisodeUrlsUpdateRequest episodeUrlsUpdateRequest() {
        return Instancio.create(EpisodeUrlsUpdateRequest.class);
    }

}
