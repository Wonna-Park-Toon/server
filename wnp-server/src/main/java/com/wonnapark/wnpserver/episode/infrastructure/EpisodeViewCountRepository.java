package com.wonnapark.wnpserver.episode.infrastructure;

public interface EpisodeViewCountRepository {

    void increaseEpisodeViewCount(Long episodeId);

}
