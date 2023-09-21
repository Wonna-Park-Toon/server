package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.EpisodeV2;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.episode.infrastructure.EpisodeRepositoryV2;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.wonnapark.wnpserver.episode.EpisodeErrorMessage.EPISODE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EpisodeFindService implements EpisodeFindUseCase {

    private final EpisodeRepository episodeRepository;
    private final EpisodeRepositoryV2 episodeRepositoryV2;
    private final EpisodeViewService episodeViewService;

    @Transactional(readOnly = true)
    @Override
    public Page<EpisodeListFormResponse> findEpisodeListForm(Long webtoonId, Pageable pageable) {
        return episodeRepository.findAllByWebtoonId(webtoonId, pageable)
                .map(EpisodeListFormResponse::from);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EpisodeListFormResponse> findEpisodeListForm(Long userId, Long webtoonId, Pageable pageable) {
        Page<Episode> episodes = episodeRepository.findAllByWebtoonId(webtoonId, pageable);
        List<Long> episodeIds = episodes.getContent().stream()
                .map(Episode::getId)
                .toList();

        Set<Long> viewedEpisodeIds = episodeViewService.getUserViewedEpisodeIdsInPagedEpisodeIds(userId, episodeIds);
        return episodes.map(episode -> {
            boolean isViewed = viewedEpisodeIds.contains(episode.getId());
            return EpisodeListFormResponse.of(episode, isViewed);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public EpisodeDetailFormResponse findEpisodeDetailForm(String ip, Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(EPISODE_NOT_FOUND.getMessage(episodeId)));

        episodeViewService.saveViewInfo(ip, episode.getId());

        return EpisodeDetailFormResponse.from(episode);
    }

    @Transactional(readOnly = true)
    @Override
    public EpisodeDetailFormResponse findEpisodeDetailFormV2(String ip, Long episodeId) {
        EpisodeV2 episode = episodeRepositoryV2.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(EPISODE_NOT_FOUND.getMessage(episodeId)));

        episodeViewService.saveViewInfo(ip, episode.getId());

        return EpisodeDetailFormResponse.fromV2(episode);
    }

    @Transactional(readOnly = true)
    @Override
    public EpisodeDetailFormResponse findEpisodeDetailForm(Long userId, Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(EPISODE_NOT_FOUND.getMessage(episodeId)));

        episodeViewService.saveViewInfo(userId, episode.getId());

        return EpisodeDetailFormResponse.from(episode);
    }

    @Override
    public EpisodeDetailFormResponse findEpisodeDetailFormV2(Long userId, Long episodeId) {
        EpisodeV2 episode = episodeRepositoryV2.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(EPISODE_NOT_FOUND.getMessage(episodeId)));

        episodeViewService.saveViewInfo(userId, episode.getId());

        return EpisodeDetailFormResponse.fromV2(episode);
    }

}
