package com.wonnapark.wnpserver.episode.infrastructure;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.EpisodeFixtures;
import com.wonnapark.wnpserver.episode.ViewHistory;
import com.wonnapark.wnpserver.user.User;
import com.wonnapark.wnpserver.user.infrastructure.UserRepository;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class ViewHistoryRepositoryTest {

    @Autowired
    private ViewHistoryRepository viewHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebtoonRepository webtoonRepository;
    @Autowired
    private EpisodeRepository episodeRepository;
    private Webtoon webtoon;
    private User user;

    @BeforeEach
    void init() {
        webtoon = webtoonRepository.save(EpisodeFixtures.webtoon());
        user = userRepository.save(EpisodeFixtures.user());
    }

    @Test
    @DisplayName("유저 ID와 에피소드 ID에 해당하는 VIEW_HISTORY가 있을 때 true를 반환힐 수 있다.")
    void existsByUserIdAndEpisodeId_true() {
        // given
        Episode episode = episodeRepository.save(EpisodeFixtures.episode(webtoon));
        ViewHistory viewHistory = viewHistoryRepository.save(EpisodeFixtures.viewHistory(user.getId(), episode.getId()));
        // when
        boolean result = viewHistoryRepository.existsById_UserIdAndId_EpisodeId(user.getId(), episode.getId());
        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유저 ID와 에피소드 ID에 해당하는 VIEW_HISTORY가 없을 때 false를 반환힐 수 있다.")
    void existsByUserIdAndEpisodeId_false() {
        // given
        Long fakeUserId = Instancio.create(Long.class);
        Long fakeEpisodeId = Instancio.create(Long.class);
        // when
        boolean result = viewHistoryRepository.existsById_UserIdAndId_EpisodeId(fakeUserId, fakeEpisodeId);
        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주어진 유저 ID와 에피소드 ID들에 대한 VIEW_HISTORY가 존재하면 일치하는 에피소드 ID들 반환할 수 있다")
    void findEpisodeIdsInGivenEpisodeIdsByUserId_viewedEpisodeIds() {
        // given
        List<Episode> episodes = episodeRepository.saveAll(EpisodeFixtures.episodes(webtoon));
        List<Long> episodeIds = episodes.stream().map(Episode::getId).distinct().toList();
        List<ViewHistory> viewHistories = viewHistoryRepository.saveAll(EpisodeFixtures.viewHistories(user.getId(), episodeIds));
        // when
        List<Long> viewedEpisodeIds = viewHistoryRepository.findEpisodeIdsInGivenEpisodeIdsByUserId(user.getId(), episodeIds);
        // then
        assertThat(viewedEpisodeIds).containsExactlyInAnyOrderElementsOf(episodeIds);
    }


    @Test
    @DisplayName("주어진 유저 ID와 에피소드 ID들에 대한 VIEW_HISTORY가 존재하지 않으면 빈 리스트를 반환할 수 있다")
    void findEpisodeIdsInGivenEpisodeIdsByUserId_emptyList() {
        // given
        List<Episode> episodes = EpisodeFixtures.episodes(webtoon);
        List<Long> episodeIds = episodes.stream().map(Episode::getId).toList();
        // when
        List<Long> viewedEpisodeIds = viewHistoryRepository.findEpisodeIdsInGivenEpisodeIdsByUserId(user.getId(), episodeIds);
        // then
        assertThat(viewedEpisodeIds).isEmpty();
    }

}
