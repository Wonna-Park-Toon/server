package com.wonnapark.wnpserver.episode.dto.response;

import com.wonnapark.wnpserver.episode.EpisodeUrl;

public record EpisodeUrlResponse(
        String url
) {

    public static EpisodeUrlResponse from(EpisodeUrl episodeUrl) {
        return new EpisodeUrlResponse(episodeUrl.getUrl());
    }

}
