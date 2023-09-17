package com.wonnapark.wnpserver.webtoon.infrastructure;

import com.wonnapark.wnpserver.webtoon.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {

    List<Webtoon> findByPublishDaysContains(DayOfWeek publishDay);
}
