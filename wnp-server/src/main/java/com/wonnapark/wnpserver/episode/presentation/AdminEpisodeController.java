package com.wonnapark.wnpserver.episode.presentation;

import com.wonnapark.wnpserver.episode.application.EpisodeImageService;
import com.wonnapark.wnpserver.episode.application.EpisodeManageUseCase;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeUrlsUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.LastEpisodeUrlUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeCreationResponse;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeImagesUploadResponse;
import com.wonnapark.wnpserver.global.auth.Admin;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.global.utils.FileUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/admin/episode")
@RequiredArgsConstructor
public class AdminEpisodeController {

    private final EpisodeManageUseCase episodeManageUseCase;
    private final EpisodeImageService episodeImageService;

//    @Admin
    @PostMapping("/images")
    @ResponseStatus(CREATED)
    public ApiResponse<EpisodeImagesUploadResponse> createEpisodeImages(
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
        response.setHeader(LOCATION, "/api/v1/admin/episode");

        return ApiResponse.from(episodeImageService.uploadEpisodeMedia(
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
            @RequestBody @Valid EpisodeCreationRequest episodeCreationRequest,
            HttpServletResponse response
    ) {
        Long episodeId = episodeManageUseCase.createEpisode(webtoonId, episodeCreationRequest);

        String uri = URI.create(String.format("/api/v1/common/episode/detail/%d", episodeId)).toString();
        response.setHeader(LOCATION, uri);

        return ApiResponse.from(new EpisodeCreationResponse(episodeId));
    }


    @Admin
    @PatchMapping("{id}/title")
    @ResponseStatus(OK)
    public void updateEpisodeTitle(@PathVariable Long id, @RequestBody @Valid EpisodeTitleUpdateRequest request) {
        episodeManageUseCase.updateEpisodeTitle(id, request);
    }

    @Admin
    @PatchMapping("/{id}/artist-comment")
    @ResponseStatus(OK)
    public void updateEpisodeArtistComment(@PathVariable Long id, @RequestBody @Valid EpisodeArtistCommentUpdateRequest request) {
        episodeManageUseCase.updateEpisodeArtistComment(id, request);
    }

    @Admin
    @PatchMapping("/{id}/thumbnail")
    @ResponseStatus(OK)
    public void updateEpisodeThumbnail(@PathVariable Long id, @RequestBody @Valid EpisodeThumbnailUpdateRequest request) {
        episodeManageUseCase.updateEpisodeThumbnail(id, request);
    }

    @Admin
    @PatchMapping("/{id}/release-datetime")
    @ResponseStatus(OK)
    public void updateEpisodeReleaseDateTime(@PathVariable Long id, @RequestBody @Valid EpisodeReleaseDateTimeUpdateRequest request) {
        episodeManageUseCase.updateEpisodeReleaseDateTime(id, request);
    }

//    @Admin
    @PatchMapping("/{id}/image-urls")
    @ResponseStatus(OK)
    public void updateEpisodeUrls(@PathVariable Long id, @RequestBody @Valid EpisodeUrlsUpdateRequest request) {
        episodeManageUseCase.updateEpisodeUrls(id, request);
    }

//    @Admin
    @PatchMapping("/{id}/last-image-url")
    @ResponseStatus(OK)
    public void updateLastEpisodeUrl(@PathVariable Long id, @RequestBody @Valid LastEpisodeUrlUpdateRequest request) {
        episodeManageUseCase.updateLastEpisodeUrl(id, request);
    }

//    @Admin
    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void deleteEpisode(@PathVariable Long id) {
        episodeManageUseCase.deleteEpisode(id);
    }

}
