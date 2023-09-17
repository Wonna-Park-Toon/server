package com.wonnapark.wnpserver.episode.dto.response;

import com.wonnapark.wnpserver.episode.Episode;

public record EpisodeCreationResponse(
        Long id
) {

    public static EpisodeCreationResponse from(Episode episode) {
        return new EpisodeCreationResponse(episode.getId());
    }

}
