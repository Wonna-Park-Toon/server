package com.wonnapark.wnpserver.episode;

import com.wonnapark.wnpserver.global.common.BaseEntity;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "episodes")
@Entity
@Where(clause = "is_deleted = false")
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

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

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

    public void increaseViewCount() {
        this.viewCount = viewCount + 1;
    }

    public void delete() {
        isDeleted = true;
    }

    public void setEpisodeUrls(List<EpisodeUrl> episodeUrls) {
        episodeUrls.forEach(episodeUrl -> episodeUrl.setEpisode(this));
    }

}
