package com.wonnapark.wnpserver.webtoon.application;

import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.WebtoonFixtures;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonSimpleResponse;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonQueryRepository;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.DayOfWeek;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atMostOnce;

@ExtendWith(MockitoExtension.class)
class DefaultWebtoonServiceTest {

    @InjectMocks
    private DefaultWebtoonService defaultWebtoonService;
    @Mock
    private WebtoonRepository webtoonRepository;
    @Mock
    private WebtoonQueryRepository webtoonQueryRepository;

    @Test
    @DisplayName("전체 웹툰을 페이지 조회할 수 있다.")
    void findAllWebtoonsWithPaging() {
        // given
        Pageable pageable = PageRequest.of(0, 20, Sort.by("title").ascending());
        List<Webtoon> webtoons = WebtoonFixtures.createWebtoons();
        Page<Webtoon> page = new PageImpl<>(webtoons, pageable, webtoons.size());
        given(webtoonRepository.findAll(pageable)).willReturn(page);

        // when
        Page<WebtoonSimpleResponse> response = defaultWebtoonService.findAllWebtoonsWithPaging(pageable);

        // then
        assertThat(response.getTotalElements()).isEqualTo(Math.min(webtoons.size(), pageable.getPageSize()));
    }

    @ParameterizedTest
    @EnumSource(DayOfWeek.class)
    @DisplayName("요일별 웹툰 목록을 조회할 수 있다.")
    void findWebtoonsByPublishDay(DayOfWeek publishDay) {
        // given
        List<Webtoon> webtoonsOnPublishDay = WebtoonFixtures.createWebtoonsOnPublishDay(publishDay);
        given(webtoonQueryRepository.findWebtoonsByPublishDayOrderByLatestViewCount(eq(publishDay))).willReturn(webtoonsOnPublishDay);

        // when
        List<WebtoonSimpleResponse> responsesOnDayOfWeek = defaultWebtoonService.findWebtoonsByPublishDayOrderByViewCount(publishDay);

        // then
        then(webtoonQueryRepository).should(atMostOnce()).findWebtoonsByPublishDayOrderByLatestViewCount(publishDay);
        assertThat(responsesOnDayOfWeek).isEqualTo(webtoonsOnPublishDay.stream().map(WebtoonSimpleResponse::from).toList());
        // TODO: 2023-09-17 정렬 결과 검증 추가
    }

    // TODO: 2023-09-03 findAllWebtoonsForEachDayOfWeek 테스트 코드 추가

}
