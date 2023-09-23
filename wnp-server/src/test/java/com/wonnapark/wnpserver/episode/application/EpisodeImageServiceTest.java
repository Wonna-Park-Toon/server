package com.wonnapark.wnpserver.episode.application;

import com.wonnapark.wnpserver.episode.dto.response.EpisodeImagesUploadResponse;
import com.wonnapark.wnpserver.media.S3MediaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class EpisodeImageServiceTest {

    @InjectMocks
    EpisodeImageService episodeImageService;
    @Mock
    S3MediaService s3MediaService;

    @Test
    void uploadEpisodeMedia() {
        // given
        String webtoonId = "1";
        File thumbnail = new File("path/thumbnail");
        List<File> episodeImages = new ArrayList<>(Arrays.asList(
                new File("path/image1"),
                new File("path/image2"),
                new File("path/image3"),
                new File("path/image4"),
                new File("path/image5")
        ));
        String expectedThumbnailUrl = "https://sample.thumbnail.jpeg";
        List<String> expectedImageUrls = IntStream.range(1, episodeImages.size() + 1)
                .mapToObj(operand -> String.format("https://sample.image_%d.jpeg", operand))
                .toList();

        given(s3MediaService.upload(anyString(), eq(thumbnail))).willReturn(expectedThumbnailUrl);
        IntStream.range(0, 5).forEach(i ->
                given(s3MediaService.upload(anyString(), eq(episodeImages.get(i)))).willReturn(expectedImageUrls.get(i))
        );

        // when
        EpisodeImagesUploadResponse episodeImagesUploadResponse = episodeImageService.uploadEpisodeMedia(webtoonId, thumbnail, episodeImages);

        // then
        assertThat(episodeImagesUploadResponse.thumbnailUrl()).isEqualTo(expectedThumbnailUrl);
        assertThat(episodeImagesUploadResponse.urls()).isEqualTo(expectedImageUrls);

        int thumbnailUploadTime = 1;
        then(s3MediaService).should(
                times(thumbnailUploadTime + episodeImages.size())
        ).upload(anyString(), any(File.class));
    }

}
