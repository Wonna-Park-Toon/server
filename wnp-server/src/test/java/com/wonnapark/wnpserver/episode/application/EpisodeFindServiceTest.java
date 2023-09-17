package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.user.User;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.wonnapark.wnpserver.episode.EpisodeFixtures.episode;
import static com.wonnapark.wnpserver.episode.EpisodeFixtures.episodes;
import static com.wonnapark.wnpserver.episode.EpisodeFixtures.ipv4;
import static com.wonnapark.wnpserver.episode.EpisodeFixtures.pageable;
import static com.wonnapark.wnpserver.episode.EpisodeFixtures.user;
import static com.wonnapark.wnpserver.episode.EpisodeFixtures.webtoon;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atMostOnce;

@ExtendWith(MockitoExtension.class)
class EpisodeFindServiceTest {
    @InjectMocks
    private EpisodeFindService episodeFindService;
    @Mock
    private EpisodeRepository episodeRepository;
    @Mock
    private EpisodeViewService episodeViewService;
    private Webtoon webtoon;

    @BeforeEach
    void init() {
        webtoon = webtoon();
    }

    @Test
    @DisplayName("웹툰 ID를 통해 에피소드 리스트 폼을 페이지 조회할 수 있다.")
    void commonFindEpisodeListForm() {
        // given
        Pageable pageable = pageable();
        List<Episode> episodes = episodes(webtoon);
        int total = Math.min(episodes.size(), pageable.getPageSize());
        List<Episode> pagedEpisode = episodes.subList(0, total);

        Page<Episode> page = new PageImpl<>(pagedEpisode, pageable, total);
        given(episodeRepository.findAllByWebtoonId(webtoon.getId(), pageable)).willReturn(page);

        // when
        Page<EpisodeListFormResponse> episodeListForm = episodeFindService.findEpisodeListForm(webtoon.getId(), pageable);

        // then
        assertThat(episodeListForm.getNumberOfElements()).isEqualTo(total);
    }

    @Test
    @DisplayName("에피소드 ID를 통해 에피소드 디테일 폼을 조회할 수 있다.")
    void commonFindEpisodeDetailForm() {
        // given
        String ip = ipv4();
        Episode episode = episode(webtoon);
        given(episodeRepository.findById(episode.getId())).willReturn(Optional.of(episode));

        // when
        EpisodeDetailFormResponse episodeDetailForm = episodeFindService.findEpisodeDetailForm(ip, episode.getId());

        // then
        assertThat(episodeDetailForm.title()).isEqualTo(episode.getTitle());
        then(episodeViewService).should(atMostOnce()).saveViewInfo(ip, episode.getId());
    }

    @Test
    @DisplayName("유저 ID와 웹툰 ID를 통해 이미 본 에피소드에 대한 정보를 포함한 에피소드 리스트를 페이지 조회할 수 있다.")
    void userFindEpisodeListForm() {
        // given
        User user = user();
        Pageable pageable = pageable();

        List<Episode> episodes = episodes(webtoon);
        int total = Math.min(episodes.size(), pageable.getPageSize());
        List<Episode> pagedEpisodes = episodes.subList(0, total);

        Page<Episode> page = new PageImpl<>(pagedEpisodes, pageable, total);
        given(episodeRepository.findAllByWebtoonId(webtoon.getId(), pageable)).willReturn(page);

        // when
        Page<EpisodeListFormResponse> episodeListForm = episodeFindService.findEpisodeListForm(user.getId(), webtoon.getId(), pageable);

        // then
        assertThat(episodeListForm.getContent()).isNotEmpty();
        assertThat(episodeListForm.getContent()).allMatch(response -> !response.isViewed());
        assertThat(episodeListForm.getTotalElements()).isEqualTo(total);
        then(episodeViewService).should(atMostOnce()).getUserViewedEpisodeIdsInPagedEpisodeIds(
                user.getId(), pagedEpisodes.stream().map(Episode::getId).toList()
        );
    }

    @Test
    @DisplayName("유저 ID와 에피소드 ID를 통해 에피소드 디테일 폼을 조회할 수 있다.")
    void userFindEpisodeDetailForm() {
        // given
        User user = user();
        Episode episode = episode(webtoon);
        given(episodeRepository.findById(episode.getId())).willReturn(Optional.of(episode));

        // when
        EpisodeDetailFormResponse episodeDetailForm = episodeFindService.findEpisodeDetailForm(user.getId(), episode.getId());

        // then
        assertThat(episodeDetailForm.id()).isEqualTo(episode.getId());
        then(episodeViewService).should(atMostOnce()).saveViewInfo(user.getId(), episode.getId());
    }

}
