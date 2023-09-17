package com.wonnapark.wnpserver.webtoon.dto.request;

import com.wonnapark.wnpserver.webtoon.AgeRating;
import com.wonnapark.wnpserver.webtoon.Webtoon;

import java.time.DayOfWeek;
import java.util.List;

public record WebtoonCreateDetailRequest(
        String title,
        String artist,
        String summary,
        String genre,
        String ageRating,
        List<DayOfWeek> publishDays
) {
    public static Webtoon toEntity(WebtoonCreateDetailRequest request) {
        return Webtoon.builder()
                .title(request.title())
                .artist(request.artist())
                .summary(request.summary())
                .genre(request.genre())
                .ageRating(AgeRating.from(request.ageRating()))
                .publishDays(request.publishDays())
                .build();
    }

}
