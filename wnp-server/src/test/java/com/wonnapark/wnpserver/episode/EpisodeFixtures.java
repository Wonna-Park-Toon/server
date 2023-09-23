package com.wonnapark.wnpserver.episode;

import com.wonnapark.wnpserver.episode.dto.request.WebtoonListPageRequest;
import com.wonnapark.wnpserver.user.User;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import org.instancio.Instancio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static org.instancio.Assign.valueOf;
import static org.instancio.Select.field;

public final class EpisodeFixtures {

    private EpisodeFixtures() {
    }

    public static String ipv4() {
        Random random = new Random();
        int first = random.nextInt(256);  // 0~255
        int second = random.nextInt(256);  // 0~255
        int third = random.nextInt(256);  // 0~255
        int fourth = random.nextInt(256);  // 0~255
        return String.join(".",
                String.valueOf(first), String.valueOf(second), String.valueOf(third), String.valueOf(fourth)
        );
    }

    public static Webtoon webtoon() {
        return Instancio.of(Webtoon.class)
                .ignore(field(Webtoon::getIsDeleted))
                .create();
    }

    public static User user() {
        return Instancio.of(User.class)
                .ignore(field(User::getIsDeleted))
                .create();
    }

    public static Episode episode(Webtoon webtoon) {
        Episode episode = Instancio.of(Episode.class)
                .set(field(Episode::getWebtoon), webtoon)
                .ignore(field(Episode::getEpisodeUrls))
                .create();
        episode.setEpisodeUrls(episodeUrls(1L));
        return episode;
    }

    public static List<Episode> episodes(Webtoon webtoon) {

        List<Episode> episodes = Instancio.ofList(Episode.class)
                .set(field(Episode::getWebtoon), webtoon)
                .assign(valueOf(Episode::getId).generate(gen -> gen.longSeq().start(1L)))
                .ignore(field(Episode::getEpisodeUrls))
                .create();

        AtomicReference<Long> episodeUrlsId = new AtomicReference<>(1L);
        episodes.forEach(episode -> {
            episode.setEpisodeUrls(episodeUrls(episodeUrlsId.get()));
            episodeUrlsId.updateAndGet(v -> v + episode.getEpisodeUrls().size());
        });
        return episodes;
    }

    public static ViewHistory viewHistory(Long userId, Long episodeId) {
        return ViewHistory.builder()
                .userId(userId)
                .episodeId(episodeId)
                .build();
    }

    public static List<ViewHistory> viewHistories(Long userId, List<Long> episodeIds) {
        return episodeIds.stream().map(
                episodeId -> ViewHistory.builder()
                        .userId(userId)
                        .episodeId(episodeId)
                        .build()
        ).toList();
    }

    public static List<EpisodeUrl> episodeUrls(Long initialEpisodeUrl) {
        return Instancio.ofList(EpisodeUrl.class)
                .assign(valueOf(EpisodeUrl::getId).generate(gen -> gen.longSeq().start(initialEpisodeUrl)))
                .ignore(field(EpisodeUrl::getEpisode))
                .create();
    }

    public static Pageable pageable() {
        return new WebtoonListPageRequest(0, Sort.Direction.DESC).toPageable();
    }

}
