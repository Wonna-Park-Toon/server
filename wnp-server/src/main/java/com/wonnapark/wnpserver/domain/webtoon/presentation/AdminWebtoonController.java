package com.wonnapark.wnpserver.domain.webtoon.presentation;

import com.wonnapark.wnpserver.domain.webtoon.application.AdminWebtoonService;
import com.wonnapark.wnpserver.domain.webtoon.dto.request.WebtoonCreateRequest;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("api/v1/admin/webtoons")
@RequiredArgsConstructor
public class AdminWebtoonController {

    private final AdminWebtoonService adminWebtoonService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createWebtoon(@RequestBody WebtoonCreateRequest request, HttpServletResponse response) {
        Long webtoonId = adminWebtoonService.createWebtoon(request);
        String uri = URI.create(String.format("/api/v1/webtoons/%d", webtoonId)).toString();
        response.setHeader("Location", uri);

        return ApiResponse.from(webtoonId);
    }

}