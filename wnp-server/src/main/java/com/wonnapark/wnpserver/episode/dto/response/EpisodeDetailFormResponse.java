package com.wonnapark.wnpserver.episode.dto.response;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.EpisodeUrl;
import com.wonnapark.wnpserver.episode.EpisodeV2;

import java.util.List;
import java.util.stream.IntStream;

public record EpisodeDetailFormResponse(
        Long id,
        String artistComment,
        String title,
        List<EpisodeUrlResponse> episodeUrlResponses
) {

    public static EpisodeDetailFormResponse from(Episode episode) {
        List<EpisodeUrlResponse> episodeUrlResponses = episode.getEpisodeUrls().stream()
                .map(EpisodeUrlResponse::from)
                .toList();

        return new EpisodeDetailFormResponse(
                episode.getId(),
                episode.getArtistComment(),
                episode.getTitle(),
                episodeUrlResponses
        );
    }

    public static EpisodeDetailFormResponse fromV2(EpisodeV2 episode) {
        String lastEpisodeUrl = episode.getLastEpisodeUrl();
        int numberStartIndex = lastEpisodeUrl.lastIndexOf('_') + 1;

        String prefix = lastEpisodeUrl.substring(0, numberStartIndex);
        int lastEpisodeUrlOrder = Integer.parseInt(lastEpisodeUrl.substring(numberStartIndex));

        List<EpisodeUrlResponse> urls = IntStream.range(1, lastEpisodeUrlOrder + 1)
                .mapToObj(number -> new EpisodeUrlResponse(prefix + number))
                .toList();

        return new EpisodeDetailFormResponse(
                episode.getId(),
                episode.getArtistComment(),
                episode.getTitle(),
                urls
        );
    }

}
