package com.wonnapark.wnpserver.episode;

import com.wonnapark.wnpserver.episode.application.EpisodeFindService;
import com.wonnapark.wnpserver.episode.infrastructure.EpisodeRepository;
import com.wonnapark.wnpserver.episode.infrastructure.EpisodeRepositoryV2;
import com.wonnapark.wnpserver.webtoon.AgeRating;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PerformanceTest {

    private static final int NUMBER_OF_EPISODES = 10_000;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private EpisodeRepositoryV2 episodeRepositoryV2;

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Test
    @DisplayName("LAST URL")
    void LAST_URL() {
        Webtoon webtoon = webtoonRepository.save(webtoon());
        List<EpisodeV2> episodesWithLastUrl = episodesWithLastUrl(webtoon);

        runAndAverage("LAST URL Test", () -> saveTemplate(episodesWithLastUrl, episodeRepositoryV2::save), 10);
        runAndAverage("LAST URL SELECT Test", () -> selectTemplate(episodeRepositoryV2::findById), 10);
    }

    @Test
    @DisplayName("EPISODE URL")
    void EPISODE_URL() {
        Webtoon webtoon = webtoonRepository.save(webtoon());
        List<Episode> episodesWithUrl = episodesWithEpisodeUrl(webtoon);

        runAndAverage("EPISODE URL Test", () -> saveTemplate(episodesWithUrl, episodeRepository::save), 10);
        runAndAverage("EPISODE URL SELECT Test", () -> selectTemplate(episodeRepository::findById), 10);
    }

    private void runAndAverage(String label, Runnable test, int iterations) {
        long totalTime = 0;

        for (int i = 1; i <= iterations; i++) {
            System.out.print(label + " " + i + " ");
            long start = System.nanoTime();

            test.run();

            long elapsedTime = System.nanoTime() - start;
            totalTime += elapsedTime;
        }

        double averageTime = (double) totalTime / iterations / 1_000_000_000.0;
        System.out.println("####################" + label + " Average Time: " + averageTime + "####################\n");
    }

    private <T> void selectTemplate(Function<Long, Optional<T>> func) {
        long start = System.nanoTime();

        long episodeID = RandomGenerator.getDefault().nextLong(NUMBER_OF_EPISODES) + 1;

        T episode = func.apply(episodeID).orElseThrow();
        if (episode instanceof Episode) {
            List<EpisodeUrl> episodeUrls = ((Episode) episode).getEpisodeUrls();
        }

        long elapseTime = System.nanoTime() - start;
        System.out.println("Select Time: " + elapseTime / 1_000_000_000.0);
    }

    private <T> void saveTemplate(List<T> episodes, Function<T, T> func) {
        long start = System.nanoTime();

        for (T episode : episodes) {
            func.apply(episode);
        }

        long elapseTime = System.nanoTime() - start;
        System.out.println("Save Time: " + elapseTime / 1_000_000_000.0);
    }

    private Webtoon webtoon() {
        return Webtoon.builder()
                .title("제목")
                .artist("아티스트")
                .summary("요약")
                .genre("장르")
                .ageRating(AgeRating.OVER_18)
                .publishDays(List.of(DayOfWeek.MONDAY))
                .build();
    }

    private List<Episode> episodesWithEpisodeUrl(Webtoon webtoon) {
        return IntStream.range(0, NUMBER_OF_EPISODES)
                .mapToObj(number ->
                        {
                            Episode episode = Episode.builder()
                                    .title("제목" + number)
                                    .releaseDateTime(LocalDateTime.now())
                                    .thumbnail("썸네일" + number)
                                    .artistComment("작가의 말" + number)
                                    .webtoon(webtoon)
                                    .build();
                            episode.setEpisodeUrls(episodeUrls());
                            return episode;
                        }

                ).toList();
    }

    private List<EpisodeV2> episodesWithLastUrl(Webtoon webtoon) {
        return IntStream.range(0, NUMBER_OF_EPISODES)
                .mapToObj(number ->
                        EpisodeV2.builder()
                                .title("제목" + number)
                                .releaseDateTime(LocalDateTime.now())
                                .thumbnail("썸네일" + number)
                                .artistComment("작가의 말" + number)
                                .webtoon(webtoon)
                                .lastEpisodeUrl("마지막 URL")
                                .build()
                ).toList();
    }

    private List<EpisodeUrl> episodeUrls() {
        String value = String.valueOf(UUID.randomUUID());
        int numberOfEpisodeUrl = 100;
        return IntStream.range(0, numberOfEpisodeUrl)
                .mapToObj(number ->
                        new EpisodeUrl(value + number)
                ).toList();
    }

}
