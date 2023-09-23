package com.wonnapark.wnpserver.webtoon.presentation;

import com.wonnapark.wnpserver.global.auth.Authorized;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.webtoon.application.DefaultWebtoonService;
import com.wonnapark.wnpserver.webtoon.application.UserWebtoonService;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonSimpleResponse;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonsOnPublishDayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/webtoons")
@RequiredArgsConstructor
public class DefaultWebtoonController {

    private final DefaultWebtoonService defaultWebtoonService;
    private final UserWebtoonService userWebtoonService;

    @GetMapping("/{webtoonId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<WebtoonDetailResponse> findWebtoonById(@PathVariable Long webtoonId, @Authorized UserInfo userInfo) {
        WebtoonDetailResponse response = userWebtoonService.findWebtoonById(webtoonId, userInfo);
        return ApiResponse.from(response);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<WebtoonSimpleResponse>> findWebtoonsByPublishDay(
            @RequestParam DayOfWeek publishDay,
            @RequestParam(required = false) OrderOption orderOption
    ) {
        List<WebtoonSimpleResponse> data = new ArrayList<>();

        if (orderOption.equals(OrderOption.VIEW_COUNT)) {
            data = defaultWebtoonService.findWebtoonsByPublishDayOrderByViewCount(publishDay);
        } else if (orderOption.equals(OrderOption.POPULARITY)) {
            data = defaultWebtoonService.findWebtoonsByPublishDayOrderByPopularity(publishDay);
        }

        return ApiResponse.from(data);
    }

    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Page<WebtoonSimpleResponse>> findAllWebtoonsWithPaging(@PageableDefault Pageable pageable) {
        Page<WebtoonSimpleResponse> data = defaultWebtoonService.findAllWebtoonsWithPaging(pageable);

        return ApiResponse.from(data);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<WebtoonsOnPublishDayResponse>> findAllWebtoonsForEachPublishDay(
            @RequestParam(required = false) OrderOption orderOption
    ) {
        List<WebtoonsOnPublishDayResponse> data = new ArrayList<>();
        if (orderOption.equals(OrderOption.VIEW_COUNT)) {
            data = defaultWebtoonService.findAllWebtoonsOrderByViewCount();
        } else if (orderOption.equals(OrderOption.POPULARITY)) {
            data = defaultWebtoonService.findAllWebtoonsOrderByPopularity();
        }

        return ApiResponse.from(data);
    }

}
