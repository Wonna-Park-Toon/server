package com.wonnapark.wnpserver.webtoon.presentation;

import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.global.utils.FileUtils;
import com.wonnapark.wnpserver.webtoon.application.AdminWebtoonService;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonCreateDetailRequest;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonUpdateDetailRequest;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonThumbnailResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/admin/webtoons")
@RequiredArgsConstructor
public class AdminWebtoonController {

    private final AdminWebtoonService adminWebtoonService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<WebtoonDetailResponse> createWebtoonDetail(@RequestBody WebtoonCreateDetailRequest request, HttpServletResponse response) {
        WebtoonDetailResponse data = adminWebtoonService.createWebtoonDetail(request);
        String uri = URI.create(String.format("/api/v1/webtoons/%d", data.id())).toString();
        response.setHeader("Location", uri);

        return ApiResponse.from(data);
    }

    @PatchMapping("{webtoonId}/thumbnail")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<WebtoonThumbnailResponse> updateWebtoonThumbnail(
            @RequestPart("thumbnail")
            @NotNull(message = "웹툰 썸네일은 null일 수 없습니다.")
            MultipartFile thumbnailMultipartFile,
            @PathVariable
            Long webtoonId
    ){
        WebtoonThumbnailResponse response = adminWebtoonService.updateWebtoonThumbnail(
                FileUtils.convertMultipartFileToFile(thumbnailMultipartFile),
                webtoonId
        );

        return ApiResponse.from(response);
    }

    @PatchMapping("/{webtoonId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<WebtoonDetailResponse> updateWebtoonDetail(@PathVariable Long webtoonId, @RequestBody WebtoonUpdateDetailRequest request) {
        WebtoonDetailResponse response = adminWebtoonService.updateWebtoonDetail(request, webtoonId);

        return ApiResponse.from(response);
    }

    @DeleteMapping("/{webtoonId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LocalDateTime> deleteWebtoon(@PathVariable Long webtoonId) {
        LocalDateTime deletedDateTime = adminWebtoonService.deleteWebtoon(webtoonId);

        return ApiResponse.from(deletedDateTime);
    }

}
