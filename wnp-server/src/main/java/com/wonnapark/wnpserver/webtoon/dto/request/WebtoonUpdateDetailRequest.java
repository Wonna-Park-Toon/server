package com.wonnapark.wnpserver.webtoon.dto.request;

import java.time.DayOfWeek;
import java.util.List;

public record WebtoonUpdateDetailRequest(
        String title,
        String artist,
        String summary,
        String genre,
        String ageRating,
        List<DayOfWeek> publishDays
) {
}
