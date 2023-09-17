package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.ViewHistory;
import com.wonnapark.wnpserver.episode.infrastructure.ViewHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ViewHistoryServiceTest {

    @InjectMocks
    private ViewHistoryService viewHistoryService;
    @Mock
    private ViewHistoryRepository viewHistoryRepository;


    @Test
    void saveViewHistory_save() {
        // given
        Long userId = 1L;
        Long episodeId = 100L;

        given(viewHistoryRepository.existsById_UserIdAndId_EpisodeId(userId, episodeId)).willReturn(false);

        // when
        viewHistoryService.saveViewHistory(userId, episodeId);

        // then
        then(viewHistoryRepository).should().save(any(ViewHistory.class));
    }

    @Test
    void saveViewHistory_not_save() {
        // Given
        Long userId = 1L;
        Long episodeId = 100L;

        given(viewHistoryRepository.existsById_UserIdAndId_EpisodeId(userId, episodeId)).willReturn(true);

        // When
        viewHistoryService.saveViewHistory(userId, episodeId);

        // Then
        then(viewHistoryRepository).should(never()).save(any(ViewHistory.class));
    }

    @Test
    void findViewedEpisodeIdsForUser() {
        // given
        Long userId = 1L;
        List<Long> episodeIds = Arrays.asList(100L, 101L, 102L);
        List<Long> viewedIds = Arrays.asList(101L, 102L);

        given(viewHistoryRepository.findEpisodeIdsInGivenEpisodeIdsByUserId(userId, episodeIds)).willReturn(viewedIds);

        // when
        Set<Long> result = viewHistoryService.findViewedEpisodeIdsForUser(userId, episodeIds);

        // then
        assertThat(result).isEqualTo(new HashSet<>(viewedIds));
    }

}
