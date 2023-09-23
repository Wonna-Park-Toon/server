package com.wonnapark.wnpserver.webtoon.infrastructure;

import com.wonnapark.wnpserver.webtoon.Webtoon;

import java.time.DayOfWeek;
import java.util.List;

public interface WebtoonQueryRepository {

    List<Webtoon> findWebtoonsByPublishDayOrderByLatestViewCount(DayOfWeek publishDay);

    List<Webtoon> findWebtoonsByPublishDayOrderByPopularity(DayOfWeek publishDay);
}
