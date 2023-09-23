package com.wonnapark.wnpserver.episode.dto.response;

import java.util.List;

public record EpisodeImagesUploadResponse(
        String thumbnailUrl,
        List<String> urls
) {
}
