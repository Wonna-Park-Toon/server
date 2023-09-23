package com.wonnapark.wnpserver.episode;

import com.wonnapark.wnpserver.global.common.BaseEntity;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "episodes")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Episode extends BaseEntity {

    private static final int MAX_TITLE_LENGTH = 35;
    private static final int MAX_ARTIST_COMMENT_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = MAX_TITLE_LENGTH)
    private String title;

    @Column(name = "release_date", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private LocalDateTime releaseDateTime;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "artist_comment", nullable = false, length = MAX_ARTIST_COMMENT_LENGTH)
    private String artistComment;

    @Column(name = "view_count", nullable = false)
    private long viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<EpisodeUrl> episodeUrls = new ArrayList<>();

    @Builder
    private Episode(String title, LocalDateTime releaseDateTime, String thumbnail, String artistComment, Webtoon webtoon) {
        this.title = title;
        this.releaseDateTime = releaseDateTime;
        this.thumbnail = thumbnail;
        this.artistComment = artistComment;
        this.webtoon = webtoon;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeArtistComment(String artistComment) {
        this.artistComment = artistComment;
    }

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void changeReleaseDateTime(LocalDateTime releaseDateTime) {
        this.releaseDateTime = releaseDateTime;
    }

    public void changeEpisodeUrls(List<EpisodeUrl> episodeUrls) {
        this.episodeUrls.clear();
        setEpisodeUrls(episodeUrls);
    }

    public void setEpisodeUrls(List<EpisodeUrl> episodeUrls) {
        episodeUrls.forEach(episodeUrl -> episodeUrl.setEpisode(this));
    }

}
