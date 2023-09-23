package com.wonnapark.wnpserver.webtoon.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wonnapark.wnpserver.episode.QEpisode;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

import static com.wonnapark.wnpserver.episode.QEpisode.episode;
import static com.wonnapark.wnpserver.webtoon.QWebtoon.webtoon;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebtoonQueryRepositoryImpl implements WebtoonQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 해당 요일의 웹툰 목록을 최신 에피소드 조회수를 기준으로 정렬하여 가져오기
     *
     * @param publishDay 연재 요일
     * @return 최신 에피소드의 조회순으로 정렬된 해당 연재 요일의 웹툰 엔티티 리스트
     */
    @Override
    @Cacheable(key = "#publishDay", value = "webtoonsByPublishDayOrderByViewCount")
    public List<Webtoon> findWebtoonsByPublishDayOrderByLatestViewCount(DayOfWeek publishDay) {
        QEpisode episode1 = new QEpisode("episode1");
        QEpisode episode2 = new QEpisode("episode2");
        List<Webtoon> webtoons = jpaQueryFactory
                .selectFrom(webtoon)
                .innerJoin(episode1).on(webtoon.id.eq(episode1.webtoon.id))
                .leftJoin(episode2).on(webtoon.id.eq(episode2.webtoon.id).and(episode1.id.lt(episode2.id)))
                .where(episode2.isNull().and(webtoon.publishDays.contains(publishDay)))
                .orderBy(episode1.viewCount.desc())
                .fetch();

        return webtoons;
    }

    /**
     * 해당 요일의 웹툰 목록을 에피소드 조회수의 평균값을 기준으로 정렬하여 가져오기
     *
     * @param publishDay 연재 요일
     * @return 에피소드 조회수의 평균값 순으로 정렬된 해당 연재 요일의 웹툰 엔티티 리스트
     */
    @Override
    @Cacheable(key = "#publishDay", value = "webtoonsByPublishDayOrderByPopularity")
    public List<Webtoon> findWebtoonsByPublishDayOrderByPopularity(DayOfWeek publishDay) {
        return jpaQueryFactory
                .selectFrom(webtoon)
                .innerJoin(episode).on(webtoon.id.eq(episode.webtoon.id))
                .groupBy(webtoon.id)
                .orderBy(episode.viewCount.avg().desc())
                .where(webtoon.publishDays.contains(publishDay))
                .fetch();
    }

}


