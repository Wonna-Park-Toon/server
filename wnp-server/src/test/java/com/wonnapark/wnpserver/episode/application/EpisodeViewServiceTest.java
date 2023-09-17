package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.ViewCoolTime;
import com.wonnapark.wnpserver.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.episode.infrastructure.ViewCoolTimeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.wonnapark.wnpserver.episode.EpisodeFixtures.episode;
import static com.wonnapark.wnpserver.episode.EpisodeFixtures.ipv4;
import static com.wonnapark.wnpserver.episode.EpisodeFixtures.webtoon;
import static com.wonnapark.wnpserver.episode.ViewCoolTime.generateKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class EpisodeViewServiceTest {

    @InjectMocks
    private EpisodeViewService episodeViewService;
    @Mock
    private ViewCoolTimeRepository viewCoolTimeRepository;
    @Mock
    private ViewHistoryService viewHistoryService;
    @Mock
    private EpisodeRepository episodeRepository;

    @Test
    @DisplayName("회원의 레디스에 해당 키가 있으면 조회 정보를 저장하지 않을 수 있다")
    void user_saveViewInfo_true() {
        // given
        Long userId = 1L;
        Episode episode = episode(webtoon());
        String key = generateKey(userId, episode.getId());

        given(viewCoolTimeRepository.existsById(key)).willReturn(true);

        // when
        episodeViewService.saveViewInfo(userId, episode.getId());

        // then
        then(episodeRepository).should(never()).increaseEpisodeViewCount(episode.getId());
        then(viewHistoryService).should(never()).saveViewHistory(userId, episode.getId());
        then(viewCoolTimeRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("회원의 레디스에 조회 정보를 저장할 수 있다")
    void user_saveViewInfo_false() {
        // given
        Long userId = 1L;
        Episode episode = episode(webtoon());
        String key = generateKey(userId, episode.getId());

        given(viewCoolTimeRepository.existsById(key)).willReturn(false);

        // when
        episodeViewService.saveViewInfo(userId, episode.getId());

        // then
        then(episodeRepository).should(atMostOnce()).increaseEpisodeViewCount(episode.getId());
        then(viewHistoryService).should(atMostOnce()).saveViewHistory(userId, episode.getId());
        then(viewCoolTimeRepository).should(atMostOnce()).save(new ViewCoolTime(key, any()));
    }

    @Test
    @DisplayName("비회원의 조회 정보를 저장할 수 있다")
    void guest_saveViewInfo_false() {
        // given
        String ip = ipv4();
        Episode episode = episode(webtoon());
        String key = generateKey(ip, episode.getId());

        given(viewCoolTimeRepository.existsById(key)).willReturn(false);

        // when
        episodeViewService.saveViewInfo(ip, episode.getId());

        // then
        then(episodeRepository).should(atMostOnce()).increaseEpisodeViewCount(episode.getId());
        then(viewCoolTimeRepository).should(atMostOnce()).save(any(ViewCoolTime.class));
    }

    @Test
    @DisplayName("비회원의 레디스에 해당 키가 있으면 조회 정보를 저장하지 않을 수 있다")
    void guest_saveViewInfo_true() {
        // given
        String ip = ipv4();
        Episode episode = episode(webtoon());
        String key = generateKey(ip, episode.getId());

        given(viewCoolTimeRepository.existsById(key)).willReturn(true);

        // when
        episodeViewService.saveViewInfo(ip, episode.getId());

        // then
        then(episodeRepository).should(never()).increaseEpisodeViewCount(episode.getId());
        then(viewCoolTimeRepository).should(never()).save(any(ViewCoolTime.class));
    }

}
