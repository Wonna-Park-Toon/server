package com.wonnapark.wnpserver.episode.presentation;

import com.wonnapark.wnpserver.episode.application.EpisodeImageService;
import com.wonnapark.wnpserver.episode.application.EpisodeManageUseCase;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeCreationRequestV2;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeCreationResponse;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeImagesUploadResponseV2;
import com.wonnapark.wnpserver.global.auth.Admin;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.global.utils.FileUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v2/admin/episode")
@RequiredArgsConstructor
public class AdminEpisodeControllerV2 {

    private final EpisodeManageUseCase episodeManageUseCase;
    private final EpisodeImageService episodeImageService;

//    @Admin
    @PostMapping("/images")
    @ResponseStatus(CREATED)
    public ApiResponse<EpisodeImagesUploadResponseV2> createEpisodeImages(
            @RequestParam("webtoonId")
            @NotNull(message = "웹툰 ID는 null일 수 없습니다.")
            String webtoonId,
            @RequestPart("thumbnail")
            @NotNull(message = "에피소드 썸네일은 null일 수 없습니다.")
            MultipartFile thumbnail,
            @RequestPart("episodeImages")
            @NotNull(message = "에피소드 이미지는 null일 수 없습니다.")
            List<MultipartFile> episodeImages,
            HttpServletResponse response
    ) {
        response.setHeader(LOCATION, "/api/v2/admin/episode");

        return ApiResponse.from(episodeImageService.uploadEpisodeMediaV2(
                webtoonId,
                FileUtils.convertMultipartFileToFile(thumbnail),
                episodeImages.stream()
                        .map(FileUtils::convertMultipartFileToFile).toList()
        ));
    }

//    @Admin
    @PostMapping
    @ResponseStatus(CREATED)
    public ApiResponse<EpisodeCreationResponse> createEpisode(
            @RequestParam Long webtoonId,
            @RequestBody @Valid EpisodeCreationRequestV2 episodeCreationRequest,
            HttpServletResponse response
    ) {
        Long episodeId = episodeManageUseCase.createEpisodeV2(webtoonId, episodeCreationRequest);

        URI uri = URI.create(String.format("/api/v1/common/episode/detail/%d", episodeId));
        response.setHeader(LOCATION, String.valueOf(uri));

        return ApiResponse.from(new EpisodeCreationResponse(episodeId));
    }

}
