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
import java.util.ArrayList;
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

    public List<WebtoonsOnPublishDayResponse> findAllWebtoonsForEachDayOfWeek() {
        List<WebtoonsOnPublishDayResponse> responseList = new ArrayList<>();
        for (DayOfWeek publishDay : DayOfWeek.values()) {
            responseList.add(WebtoonsOnPublishDayResponse.of(publishDay, findWebtoonsByPublishDayInView(publishDay)));
        }

        return responseList;
    }

    public List<WebtoonSimpleResponse> findWebtoonsByPublishDayInView(DayOfWeek publishDay) {
        return webtoonQueryRepository.findWebtoonsByPublishDayInViewCount(publishDay).stream()
                .map(WebtoonSimpleResponse::from)
                .toList();
    }

    public List<WebtoonSimpleResponse> findWebtoonsByPublishDayInPopularity(DayOfWeek publishDay){
        return webtoonQueryRepository.findWebtoonsByPublishDayInPopularity(publishDay).stream()
                .map(WebtoonSimpleResponse::from)
                .toList();
    }

}
