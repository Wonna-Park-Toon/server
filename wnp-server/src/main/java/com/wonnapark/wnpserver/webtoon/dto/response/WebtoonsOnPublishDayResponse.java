package com.wonnapark.wnpserver.webtoon.dto.response;

import java.time.DayOfWeek;
import java.util.List;

public record WebtoonsOnPublishDayResponse(
        DayOfWeek publishDay,
        List<WebtoonSimpleResponse> webtoons
) {
    public static WebtoonsOnPublishDayResponse of(DayOfWeek publishDay, List<WebtoonSimpleResponse> webtoons) {
        return new WebtoonsOnPublishDayResponse(
                publishDay,
                webtoons
        );
    }
}
