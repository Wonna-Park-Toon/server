package com.wonnapark.wnpserver.webtoon;

import com.wonnapark.wnpserver.global.common.BaseEntity;
import com.wonnapark.wnpserver.webtoon.infrastructure.AgeRatingConverter;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "webtoons")
@Entity
@Getter
@Where(clause = "is_deleted IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Webtoon extends BaseEntity {

    private static final int MAX_TITLE_LENGTH = 50;
    private static final int MAX_ARTIST_LENGTH = 50;
    private static final int MAX_SUMMARY_LENGTH = 200;
    private static final int MAX_GENRE_LENGTH = 50;
    private static final String DEFAULT_WEBTOON_THUMBNAIL = "https://wonnapark-bucket.s3.ap-northeast-2.amazonaws.com/webtoon/thumbnail_default.jpg";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = MAX_TITLE_LENGTH)
    private String title;

    @Column(name = "artist", nullable = false, length = MAX_ARTIST_LENGTH)
    private String artist;

    @Column(name = "summary", nullable = false, length = MAX_SUMMARY_LENGTH)
    private String summary;

    @Column(name = "genre", nullable = false, length = MAX_GENRE_LENGTH)
    private String genre;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "age_rating", nullable = false)
    @Convert(converter = AgeRatingConverter.class)
    private AgeRating ageRating;

    @ElementCollection
    @CollectionTable(name = "publish_days", joinColumns = @JoinColumn(name = "webtoon_id"))
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> publishDays = new ArrayList<>();

    @Column(name = "is_deleted", nullable = true)
    private LocalDateTime isDeleted;

    @Builder
    private Webtoon(String title, String artist, String summary, String genre, AgeRating ageRating, List<DayOfWeek> publishDays) {
        this.title = title;
        this.artist = artist;
        this.summary = summary;
        this.genre = genre;
        this.thumbnail = DEFAULT_WEBTOON_THUMBNAIL;
        this.ageRating = ageRating;
        this.publishDays = new ArrayList<DayOfWeek>(publishDays);
    }

    /**
     * Webtoon thumbnail을 제외한 멤버 변수를 새로운 값으로 변경하는 메서드
     */
    public void changeDetail(String title, String artist, String summary, String genre, String ageRating, List<DayOfWeek> publishDays) {
        this.title = title;
        this.artist = artist;
        this.summary = summary;
        this.genre = genre;
        this.ageRating = AgeRating.from(ageRating);
        this.publishDays = new ArrayList<DayOfWeek>(publishDays);
    }

    /**
     * Webtoon thumbnail URL을 사로운 값으로 변경하는 메서드
     * @param thumbnail 새로운 썸네일 URL
     */
    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return 연령 제한이 성인 이상이면 true, 그렇지 않다면 false
     */
    public boolean isXRated() {

        if (this.ageRating.equals(AgeRating.OVER_18)) return true;

        return false;
    }

    public void delete() {
        this.isDeleted = LocalDateTime.now();
    }

}
