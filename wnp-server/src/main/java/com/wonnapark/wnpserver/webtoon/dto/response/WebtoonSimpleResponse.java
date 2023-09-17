package com.wonnapark.wnpserver.webtoon.dto.response;

import com.wonnapark.wnpserver.webtoon.Webtoon;

public record WebtoonSimpleResponse(
        Long id,
        String title,
        String artist,
        String thumbnail,
        String ageRating
) {

    public static WebtoonSimpleResponse from(Webtoon webtoon) {
        return new WebtoonSimpleResponse(
                webtoon.getId(),
                webtoon.getTitle(),
                webtoon.getArtist(),
                webtoon.getThumbnail(),
                webtoon.getAgeRating().getRatingName()
        );
    }
}
