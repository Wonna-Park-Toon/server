package com.wonnapark.wnpserver.episode.infrastructure;

import com.wonnapark.wnpserver.episode.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViewHistoryRepository extends JpaRepository<ViewHistory, ViewHistory.ViewHistoryId> {

    boolean existsById_UserIdAndId_EpisodeId(Long userId, Long episodeId);

    @Query("SELECT v.id.episodeId FROM ViewHistory v WHERE v.id.userId = :userId AND v.id.episodeId IN :episodeIds")
    List<Long> findEpisodeIdsInGivenEpisodeIdsByUserId(Long userId, List<Long> episodeIds);

}
