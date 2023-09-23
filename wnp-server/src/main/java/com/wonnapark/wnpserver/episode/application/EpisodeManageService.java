package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.dto.request.*;
import com.wonnapark.wnpserver.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wonnapark.wnpserver.episode.EpisodeErrorMessage.*;


@Service
@RequiredArgsConstructor
@Transactional
public class EpisodeManageService implements EpisodeManageUseCase {

    private final EpisodeRepository episodeRepository;
    private final WebtoonRepository webtoonRepository;

    @Override
    public Long createEpisode(Long webtoonId, EpisodeCreationRequest request) {
        if (episodeRepository.existsByWebtoonIdAndTitle(webtoonId, request.title())) {
            throw new EntityExistsException(DUPLICATED_EPISODE_TITLE.getMessage(webtoonId, request.title()));
        }
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException(WEBTOON_NOT_FOUND.getMessage(webtoonId)));
        Episode newEpisode = request.toEntity(webtoon);
        return episodeRepository.save(newEpisode).getId();
    }

    @Override
    public void updateEpisodeTitle(Long episodeId, EpisodeTitleUpdateRequest request) {
        Episode episode = findEpisodeById(episodeId);
        episode.changeTitle(request.title());
    }

    @Override
    public void updateEpisodeArtistComment(Long episodeId, EpisodeArtistCommentUpdateRequest request) {
        Episode episode = findEpisodeById(episodeId);
        episode.changeArtistComment(request.artistComment());
    }

    @Override
    public void updateEpisodeThumbnail(Long episodeId, EpisodeThumbnailUpdateRequest request) {
        Episode episode = findEpisodeById(episodeId);
        episode.changeThumbnail(request.thumbnail());
    }

    @Override
    public void updateEpisodeReleaseDateTime(Long episodeId, EpisodeReleaseDateTimeUpdateRequest request) {
        Episode episode = findEpisodeById(episodeId);
        episode.changeReleaseDateTime(request.releaseDateTime());
    }

    @Override
    public void updateEpisodeUrls(Long episodeId, EpisodeUrlsUpdateRequest request) {
        Episode episode = findEpisodeById(episodeId);
        episode.changeEpisodeUrls(request.toEntityList());
    }

    @Override
    public void deleteEpisode(Long episodeId) {
        episodeRepository.deleteById(episodeId);
    }

    private Episode findEpisodeById(Long episodeId) {
        return episodeRepository.findById(episodeId)
                .orElseThrow(() -> new EntityNotFoundException(EPISODE_NOT_FOUND.getMessage(episodeId)));
    }

}
