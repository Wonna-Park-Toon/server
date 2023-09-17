package com.wonnapark.wnpserver.webtoon.application;

import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.WebtoonFixtures;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.webtoon.exception.UnderageAccessDeniedException;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GuestWebtoonServiceTest {
    @InjectMocks
    private GuestWebtoonService guestWebtoonService;
    @Mock
    private WebtoonRepository webtoonRepository;

    @Test
    @DisplayName("18세 이용가가 아닌 웹툰을 단일 조회할 수 있다.")
    void findWebtoonUnder18ById() {
        // given
        Webtoon webtoonUnder18 = WebtoonFixtures.createWebtoonUnder18();
        given(webtoonRepository.findById(any(Long.class))).willReturn(Optional.of(webtoonUnder18));

        // when
        WebtoonDetailResponse response = guestWebtoonService.findWebtoonById(webtoonUnder18.getId());

        // then
        assertThat(webtoonUnder18.getId()).isEqualTo(response.id());
    }

    @Test
    @DisplayName("18세 이용가 웹툰은 단일 조회할 수 없다.")
    void findWebtoonOver18ById() {
        // given
        Webtoon webtoonOver18 = WebtoonFixtures.createWebtoonOver18();
        given(webtoonRepository.findById(any(Long.class))).willReturn(Optional.of(webtoonOver18));

        // when, then
        assertThatThrownBy(() -> guestWebtoonService.findWebtoonById(webtoonOver18.getId()))
                .isExactlyInstanceOf(UnderageAccessDeniedException.class);
    }

}
