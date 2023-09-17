package com.wonnapark.wnpserver.episode.presentation;

import com.wonnapark.wnpserver.episode.application.EpisodeFindUseCase;
import com.wonnapark.wnpserver.episode.dto.request.WebtoonListPageRequest;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.global.response.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/guest/episode")
@RequiredArgsConstructor
public class CommonEpisodeController {

    private final EpisodeFindUseCase episodeFindUseCase;

    @GetMapping("/{episodeId}/detail")
    @ResponseStatus(OK)
    public ApiResponse<EpisodeDetailFormResponse> findEpisodeDetailForm(
            @PathVariable Long episodeId,
            HttpServletRequest request
    ) {
        EpisodeDetailFormResponse episodeDetailForm = episodeFindUseCase.findEpisodeDetailForm(getClientIP(request), episodeId);
        return ApiResponse.from(episodeDetailForm);
    }

    @GetMapping("/list")
    @ResponseStatus(OK)
    public ApiResponse<PageResponse<EpisodeListFormResponse>> findEpisodeListForm(
            @RequestParam Long webtoonId,
            WebtoonListPageRequest webtoonListPageRequest
    ) {
        PageResponse<EpisodeListFormResponse> episodeListForms = PageResponse.from(episodeFindUseCase.findEpisodeListForm(webtoonId, webtoonListPageRequest.toPageable()));
        return ApiResponse.from(episodeListForms);
    }

    private String getClientIP(HttpServletRequest request) {
        String clientIPWhenUsingNginx = request.getHeader("X-Forwarded-For");
        if (clientIPWhenUsingNginx != null)
            return clientIPWhenUsingNginx;
        return request.getRemoteAddr();
    }

}
