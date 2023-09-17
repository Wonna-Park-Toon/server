package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.ViewHistory;
import com.wonnapark.wnpserver.episode.infrastructure.ViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ViewHistoryService {

    private final ViewHistoryRepository viewHistoryRepository;

    @Transactional
    public void saveViewHistory(Long userId, Long episodeId) {
        if (viewHistoryRepository.existsById_UserIdAndId_EpisodeId(userId, episodeId)) {
            return;
        }
        ViewHistory viewHistory = ViewHistory.builder()
                .userId(userId)
                .episodeId(episodeId)
                .build();
        viewHistoryRepository.save(viewHistory);
    }

    @Transactional(readOnly = true)
    public Set<Long> findViewedEpisodeIdsForUser(Long userId, List<Long> episodeIds) {
        return new HashSet<>(viewHistoryRepository.findEpisodeIdsInGivenEpisodeIdsByUserId(userId, episodeIds));
    }

}
