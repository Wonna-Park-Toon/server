package com.wonnapark.wnpserver.episode.dto.request;

import com.wonnapark.wnpserver.episode.EpisodeUrl;

import java.util.List;

public record EpisodeUrlsUpdateRequest(
        List<String> urls
) {

    public List<EpisodeUrl> toEntityList() {
        return urls.stream()
                .map(EpisodeUrl::new).toList();
    }

}
