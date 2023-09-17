package com.wonnapark.wnpserver.episode.dto.response;

import java.util.List;

public record EpisodeMediaUploadResponse(
        String thumbnailUrl,
        List<String> urls
) {
}
