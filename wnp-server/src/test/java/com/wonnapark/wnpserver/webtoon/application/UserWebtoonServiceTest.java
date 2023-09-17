package com.wonnapark.wnpserver.webtoon.application;

import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.WebtoonFixtures;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.webtoon.exception.UnderageAccessDeniedException;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import org.instancio.Instancio;
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
class UserWebtoonServiceTest {

    @InjectMocks
    private UserWebtoonService userWebtoonService;
    @Mock
    private WebtoonRepository webtoonRepository;

    @Test
    @DisplayName("18세 이상인 유저는 18세 이용가 웹툰을 단일 조회할 수 있다.")
    void findWebtoonOver18ByAdult() {
        // given
        UserInfo userInfo = WebtoonFixtures.createUserInfoOver18();

        Webtoon webtoonOver18 = WebtoonFixtures.createWebtoonOver18();
        given(webtoonRepository.findById(any(Long.class))).willReturn(Optional.of(webtoonOver18));

        // when
        WebtoonDetailResponse response = userWebtoonService.findWebtoonById(webtoonOver18.getId(), userInfo);

        // then
        assertThat(response.id()).isEqualTo(webtoonOver18.getId());
    }

    @Test
    @DisplayName("18세 미만인 유저는 18세 이용가 웹툰을 단일 조회할 수 없다.")
    void findWebtoonOver18ByUnderage() {
        // given
        UserInfo userInfo = WebtoonFixtures.createUserInfoUnder18();
        Webtoon webtoonOver18 = WebtoonFixtures.createWebtoonOver18();
        given(webtoonRepository.findById(any(Long.class))).willReturn(Optional.of(webtoonOver18));

        // when,then
        assertThatThrownBy(() -> userWebtoonService.findWebtoonById(webtoonOver18.getId(), userInfo))
                .isExactlyInstanceOf(UnderageAccessDeniedException.class);
    }

    @Test
    @DisplayName("모든 유저는 18세 이용가가 아닌 웹툰을 단일 조회할 수 있다.")
    void findWebtoonUnder18ByUnderage() {
        // given
        UserInfo userInfo = Instancio.create(UserInfo.class);
        Webtoon webtoonUnder18 = WebtoonFixtures.createWebtoonUnder18();
        given(webtoonRepository.findById(any(Long.class))).willReturn(Optional.of(webtoonUnder18));

        // when
        WebtoonDetailResponse response = userWebtoonService.findWebtoonById(webtoonUnder18.getId(), userInfo);

        // then
        assertThat(response.id()).isEqualTo(webtoonUnder18.getId());
    }

}
