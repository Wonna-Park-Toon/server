package com.wonnapark.wnpserver.episode.infrastructure;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class EpisodeViewCountRepositoryImpl implements EpisodeViewCountRepository {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public void increaseEpisodeViewCount(Long episodeId) {
        entityManager.createNativeQuery("""
                                UPDATE episodes
                                SET view_count = view_count + 1
                                WHERE id = :episodeId
                        """)
                .setParameter("episodeId", episodeId)
                .executeUpdate();
    }

}
