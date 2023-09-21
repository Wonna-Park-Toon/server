package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeCreationRequestV2;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeUrlsUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.LastEpisodeUrlUpdateRequest;

public interface EpisodeManageUseCase {

    Long createEpisode(Long webtoonId, EpisodeCreationRequest request);

    Long createEpisodeV2(Long webtoonId, EpisodeCreationRequestV2 request);

    void updateEpisodeTitle(Long episodeId, EpisodeTitleUpdateRequest request);

    void updateEpisodeArtistComment(Long episodeId, EpisodeArtistCommentUpdateRequest request);

    void updateEpisodeThumbnail(Long episodeId, EpisodeThumbnailUpdateRequest request);

    void updateEpisodeReleaseDateTime(Long episodeId, EpisodeReleaseDateTimeUpdateRequest request);

    void updateEpisodeUrls(Long episodeId, EpisodeUrlsUpdateRequest request);

    void updateLastEpisodeUrl(Long episodeId, LastEpisodeUrlUpdateRequest request);

    void deleteEpisode(Long episodeId);

}
