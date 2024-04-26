package org.example.shortsaccount.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id", updatable = false)
    private Long videoId;

    @Column(name = "member_id", nullable = false)
    private int memberId;

    @Column(name = "ad_id", nullable = false)
    private int adId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "upload_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime uploadDate;

    @Column(name = "length")
    private int length;

    @Column(name = "playback_time")
    private int playbackTime;

    @Column(name = "video_views")
    private int videoViews;

    @Column(name = "ad_views")
    private int adViews;

    @Builder
    public Video(int memberId, String title, int length, int adId) {
        this.title = title;
        this.memberId = memberId;
        this.adId = adId;
        this.length = length;
        this.uploadDate = LocalDateTime.now();
        this.playbackTime = 0;
        this.videoViews = 0;
        this.adViews = 0;
    }

    public void update(String title, int length) {
        this.title = title;
        this.length = length;
        this.uploadDate = LocalDateTime.now();
    }

    public void checkVideo() {
        this.videoViews = getVideoViews() + 1;
    }

    public void addPlayTime(int playTime) {
        this.playbackTime = getPlaybackTime() + playTime;
    }

    public void addAdViews(int playTime) {
        this.adViews = getAdViews() + (playTime / 300);
    }
}