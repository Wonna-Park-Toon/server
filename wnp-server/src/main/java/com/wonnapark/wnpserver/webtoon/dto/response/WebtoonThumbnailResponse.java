package com.wonnapark.wnpserver.webtoon.dto.response;

public record WebtoonThumbnailResponse(
        String thumbnailUrl
) {

    public static WebtoonThumbnailResponse from(String thumbnailUrl){
        return new WebtoonThumbnailResponse(
                thumbnailUrl
        );
    }

}
