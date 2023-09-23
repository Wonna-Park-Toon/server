package com.wonnapark.wnpserver.webtoon.application;

import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonSimpleResponse;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonsOnPublishDayResponse;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonQueryRepository;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultWebtoonService {

    private final WebtoonRepository webtoonRepository;
    private final WebtoonQueryRepository webtoonQueryRepository;

    public Page<WebtoonSimpleResponse> findAllWebtoonsWithPaging(Pageable pageable) {
        return webtoonRepository.findAll(pageable)
                .map(WebtoonSimpleResponse::from);
    }

    public List<WebtoonSimpleResponse> findWebtoonsByPublishDayOrderByViewCount(DayOfWeek publishDay) {
        return webtoonQueryRepository.findWebtoonsByPublishDayOrderByLatestViewCount(publishDay).stream()
                .map(WebtoonSimpleResponse::from)
                .toList();
    }

    public List<WebtoonSimpleResponse> findWebtoonsByPublishDayOrderByPopularity(DayOfWeek publishDay) {
        return webtoonQueryRepository.findWebtoonsByPublishDayOrderByPopularity(publishDay).stream()
                .map(WebtoonSimpleResponse::from)
                .toList();
    }

    public List<WebtoonsOnPublishDayResponse> findAllWebtoonsOrderByViewCount() {
        return Arrays.stream(DayOfWeek.values())
                .map(publishDay -> WebtoonsOnPublishDayResponse.of(publishDay, findWebtoonsByPublishDayOrderByViewCount(publishDay)))
                .toList();
    }

    public List<WebtoonsOnPublishDayResponse> findAllWebtoonsOrderByPopularity() {
        return Arrays.stream(DayOfWeek.values())
                .map(publishDay -> WebtoonsOnPublishDayResponse.of(publishDay, findWebtoonsByPublishDayOrderByPopularity(publishDay)))
                .toList();
    }

}
