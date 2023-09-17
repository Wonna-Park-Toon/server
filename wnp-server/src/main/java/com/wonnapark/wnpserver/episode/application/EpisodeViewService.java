package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.ViewCoolTime;
import com.wonnapark.wnpserver.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.episode.infrastructure.ViewCoolTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
public class EpisodeViewService {

    private final ViewCoolTimeRepository viewCoolTimeRepository;
    private final ViewHistoryService viewHistoryService;
    private final EpisodeRepository episodeRepository;

    @Transactional(propagation = REQUIRES_NEW)
    public void saveViewInfo(Long userId, Long episodeId) {
        String key = ViewCoolTime.generateKey(userId, episodeId);

        if (viewCoolTimeRepository.existsById(key)) {
            return;
        }

        episodeRepository.increaseEpisodeViewCount(episodeId);
        viewHistoryService.saveViewHistory(userId, episodeId);
        viewCoolTimeRepository.save(new ViewCoolTime(key, LocalDateTime.now()));
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void saveViewInfo(String ip, Long episodeId) {
        String key = ViewCoolTime.generateKey(ip, episodeId);

        if (viewCoolTimeRepository.existsById(key)) {
            return;
        }

        episodeRepository.increaseEpisodeViewCount(episodeId);
        viewCoolTimeRepository.save(new ViewCoolTime(key, LocalDateTime.now()));
    }

    public Set<Long> getUserViewedEpisodeIdsInPagedEpisodeIds(Long userId, List<Long> pagedEpisodeIds) {
        return viewHistoryService.findViewedEpisodeIdsForUser(userId, pagedEpisodeIds);
    }

}
