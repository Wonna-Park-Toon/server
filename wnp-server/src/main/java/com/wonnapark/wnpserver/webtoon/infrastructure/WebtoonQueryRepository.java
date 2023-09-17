package com.wonnapark.wnpserver.webtoon.infrastructure;

import java.time.DayOfWeek;
import java.util.List;

import com.wonnapark.wnpserver.webtoon.Webtoon;

public interface WebtoonQueryRepository {

    List<Webtoon> findWebtoonsByPublishDayInViewCount(DayOfWeek publishDay);
    List<Webtoon> findWebtoonsByPublishDayInPopularity(DayOfWeek publishDay);
}
