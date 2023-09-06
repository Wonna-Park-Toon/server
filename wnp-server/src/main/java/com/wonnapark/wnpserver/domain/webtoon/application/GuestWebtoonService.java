package com.wonnapark.wnpserver.domain.webtoon.application;

import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.domain.webtoon.exception.UnderageAccessDeniedException;
import com.wonnapark.wnpserver.domain.webtoon.infrastructure.WebtoonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wonnapark.wnpserver.domain.webtoon.application.WebtoonExceptionMessage.WEBTOON_AUTHORIZATION_REQUIRED;
import static com.wonnapark.wnpserver.domain.webtoon.application.WebtoonExceptionMessage.WEBTOON_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestWebtoonService {

    private final WebtoonRepository webtoonRepository;

    public WebtoonDetailResponse findWebtoonById(Long webtoonId) {
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(WEBTOON_NOT_FOUND.getMessage(), webtoonId)));

        if (webtoon.isXRated()) {
            throw new UnderageAccessDeniedException(String.format(WEBTOON_AUTHORIZATION_REQUIRED.getMessage(), webtoonId));
        }

        return WebtoonDetailResponse.from(webtoon);
    }

}
