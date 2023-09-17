package com.wonnapark.wnpserver.webtoon.application;

import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.webtoon.exception.UnderageAccessDeniedException;
import com.wonnapark.wnpserver.webtoon.infrastructure.WebtoonRepository;
import com.wonnapark.wnpserver.global.common.UserInfo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wonnapark.wnpserver.webtoon.application.WebtoonExceptionMessage.WEBTOON_AGE_RESTRICTED;
import static com.wonnapark.wnpserver.webtoon.application.WebtoonExceptionMessage.WEBTOON_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserWebtoonService {

    private final WebtoonRepository webtoonRepository;

    public WebtoonDetailResponse findWebtoonById(Long webtoonId, UserInfo userInfo) {
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(WEBTOON_NOT_FOUND.getMessage(), webtoonId)));

        if (webtoon.isXRated() && userInfo.age() <= webtoon.getAgeRating().getAgeLimit()) {
            throw new UnderageAccessDeniedException(String.format(WEBTOON_AGE_RESTRICTED.getMessage(), webtoonId));
        }

        return WebtoonDetailResponse.from(webtoon);
    }

}
