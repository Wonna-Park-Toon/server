package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.episode.dto.request.EpisodeUrlsUpdateRequest;
import com.wonnapark.wnpserver.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class EpisodeManageServiceTest {

    @InjectMocks
    private EpisodeManageService episodeService;
    @Mock
    private EpisodeRepository episodeRepository;
    @Mock
    private WebtoonRepository webtoonRepository;
    @Mock
    private ViewHistoryService viewHistoryService;

    @Test
    @DisplayName("에피소드를 생성할 수 있다.")
    void createEpisode() {
        // given
        EpisodeCreationRequest request = Instancio.create(EpisodeCreationRequest.class);
        Long webtoonId = 1L;
        Webtoon mockWebtoon = mock(Webtoon.class);
        Long episodeId = 100L;
        Episode mockEpisode = mock(Episode.class);

        given(webtoonRepository.findById(webtoonId)).willReturn(Optional.of(mockWebtoon));
        given(episodeRepository.existsByWebtoonIdAndTitle(anyLong(), any())).willReturn(false);
        given(episodeRepository.save(any(Episode.class))).willReturn(mockEpisode);
        given(mockEpisode.getId()).willReturn(episodeId);

        // when
        Long createdEpisodeId = episodeService.createEpisode(webtoonId, request);

        // then
        assertThat(createdEpisodeId).isEqualTo(episodeId);
    }

    @Test
    @DisplayName("에피소드 타이틀을 수정할 수 있다.")
    void updateEpisodeTitle() {
        // given
        Episode episode = mock(Episode.class);
        EpisodeTitleUpdateRequest request = Instancio.create(EpisodeTitleUpdateRequest.class);
        given(episodeRepository.findById(any(Long.class))).willReturn(Optional.of(episode));

        // when
        episodeService.updateEpisodeTitle(episode.getId(), request);

        // then
        then(episode).should(atMostOnce()).changeTitle(request.title());
    }


    @Test
    @DisplayName("에피소드 작가의 말을 수정할 수 있다.")
    void updateEpisodeArtistComment() {
        // given
        Episode episode = mock(Episode.class);
        EpisodeArtistCommentUpdateRequest request = Instancio.create(EpisodeArtistCommentUpdateRequest.class);
        given(episodeRepository.findById(any(Long.class))).willReturn(Optional.of(episode));

        // when
        episodeService.updateEpisodeArtistComment(episode.getId(), request);

        // then
        then(episode).should(atMostOnce()).changeArtistComment(request.artistComment());
    }

    @Test
    @DisplayName("에피소드 썸네일을 수정할 수 있다.")
    void updateEpisodeThumbnail() {
        // given
        Episode episode = mock(Episode.class);
        EpisodeThumbnailUpdateRequest request = Instancio.create(EpisodeThumbnailUpdateRequest.class);
        given(episodeRepository.findById(any(Long.class))).willReturn(Optional.of(episode));

        // when
        episodeService.updateEpisodeThumbnail(episode.getId(), request);

        // then
        then(episode).should(atMostOnce()).changeThumbnail(request.thumbnail());
    }

    @Test
    @DisplayName("에피소드 공개일을 수정할 수 있다.")
    void updateEpisodeReleaseDateTime() {
        // given
        Episode episode = mock(Episode.class);
        EpisodeReleaseDateTimeUpdateRequest request = Instancio.create(EpisodeReleaseDateTimeUpdateRequest.class);
        given(episodeRepository.findById(any(Long.class))).willReturn(Optional.of(episode));

        // when
        episodeService.updateEpisodeReleaseDateTime(episode.getId(), request);

        // then
        then(episode).should(atMostOnce()).changeReleaseDateTime(request.releaseDateTime());
    }

    @Test
    @DisplayName("에피소드 url을 전체 수정할 수 있다.")
    void updateEpisodeUrls() {
        // given
        Episode episode = mock(Episode.class);
        EpisodeUrlsUpdateRequest request = Instancio.create(EpisodeUrlsUpdateRequest.class);
        given(episodeRepository.findById(any(Long.class))).willReturn(Optional.of(episode));

        // when
        episodeService.updateEpisodeUrls(episode.getId(), request);

        // then
        then(episode).should(atMostOnce()).changeEpisodeUrls(request.toEntityList());
    }

    @Test
    @DisplayName("에피소드를 삭제할 수 있다.")
    void deleteEpisode() {
        // given
        Episode episode = mock(Episode.class);

        // when
        episodeService.deleteEpisode(episode.getId());

        // then
        then(episodeRepository).should(atMostOnce()).deleteById(episode.getId());
    }

}
