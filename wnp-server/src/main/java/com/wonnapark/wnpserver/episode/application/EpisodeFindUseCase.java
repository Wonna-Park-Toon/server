package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeListFormResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EpisodeFindUseCase {

    Page<EpisodeListFormResponse> findEpisodeListForm(Long webtoonId, Pageable pageable);

    Page<EpisodeListFormResponse> findEpisodeListForm(Long userId, Long webtoonId, Pageable pageable);

    EpisodeDetailFormResponse findEpisodeDetailForm(String ip, Long episodeId);

    EpisodeDetailFormResponse findEpisodeDetailFormV2(String ip, Long episodeId);

    EpisodeDetailFormResponse findEpisodeDetailForm(Long userId, Long episodeId);

    EpisodeDetailFormResponse findEpisodeDetailFormV2(Long userId, Long episodeId);

}
