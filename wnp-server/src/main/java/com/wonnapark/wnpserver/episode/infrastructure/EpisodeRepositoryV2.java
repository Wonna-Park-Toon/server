package com.wonnapark.wnpserver.episode.infrastructure;

import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.EpisodeV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepositoryV2 extends JpaRepository<EpisodeV2, Long>, EpisodeViewCountRepository {

    Page<EpisodeV2> findAllByWebtoonId(Long webtoonId, Pageable pageable);

    boolean existsByWebtoonIdAndTitle(Long webtoonId, String title);

}
