package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.dto.response.EpisodeMediaUploadResponse;
import com.wonnapark.wnpserver.media.S3MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EpisodeImageService {

    private static final String EPISODE_THUMBNAIL_KEY_PATTERN = "webtoon/%s/thumbnail_202x120_%s";
    private static final String EPISODE_URL_KEY_PATTERN = "webtoon/%s/%s_%s_IMAG01_%s";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final S3MediaService s3MediaService;

    public EpisodeMediaUploadResponse uploadEpisodeMedia(String webtoonId, File thumbnail, List<File> episodeImages) {
        return new EpisodeMediaUploadResponse(
                uploadThumbnail(webtoonId, thumbnail),
                uploadEpisodeImages(webtoonId, episodeImages)
        );
    }

    private String uploadThumbnail(String webtoonId, File thumbnail) {
        String key = String.format(EPISODE_THUMBNAIL_KEY_PATTERN, webtoonId, UUID.randomUUID());
        return s3MediaService.upload(key, thumbnail);
    }

    private List<String> uploadEpisodeImages(String webtoonId, List<File> episodeImages) {
        String uploadedDateTime = LocalDateTime.now().format(FORMATTER);
        UUID uuid = UUID.randomUUID();

        List<String> urls = new ArrayList<>();
        for (int imageOrder = 1; imageOrder <= episodeImages.size(); imageOrder++) {
            String key = String.format(EPISODE_URL_KEY_PATTERN, webtoonId, uploadedDateTime, uuid, imageOrder);
            int imagesIndex = imageOrder - 1;
            urls.add(s3MediaService.upload(key, episodeImages.get(imagesIndex)));
        }
        return urls;
    }

}
