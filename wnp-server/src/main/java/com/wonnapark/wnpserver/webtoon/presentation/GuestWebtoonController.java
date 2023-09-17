package com.wonnapark.wnpserver.webtoon.presentation;

import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.webtoon.application.GuestWebtoonService;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/guest/webtoons")
@RequiredArgsConstructor
public class GuestWebtoonController {

    private final GuestWebtoonService guestWebtoonService;

    @GetMapping("/{webtoonId}")
    public ApiResponse<WebtoonDetailResponse> findWebtoonById(@PathVariable Long webtoonId) {
        WebtoonDetailResponse data = guestWebtoonService.findWebtoonById(webtoonId);
        return ApiResponse.from(data);
    }

}
