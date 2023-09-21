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
import org.springframework.web.bind.annotation.*;

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

        if(orderOption.equals(OrderOption.VIEW)){
            data = defaultWebtoonService.findWebtoonsByPublishDayInView(publishDay);
        } else if (orderOption.equals(OrderOption.POPULARITY)) {
            data = defaultWebtoonService.findWebtoonsByPublishDayInPopularity(publishDay);
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
    public ApiResponse<List<WebtoonsOnPublishDayResponse>> findAllWebtoonsForEachDayOfWeek() {
        List<WebtoonsOnPublishDayResponse> data = defaultWebtoonService.findAllWebtoonsForEachDayOfWeek();

        return ApiResponse.from(data);
    }

}
