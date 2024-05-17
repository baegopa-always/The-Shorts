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
    private int videoId;

    @Column(name = "member_id", nullable = false)
    private int memberId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "upload_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime uploadDate;

    @Column(name = "length")
    private int length;

    @Column(name = "total_playtime")
    private int totalPlaytime;

    @Column(name = "video_views")
    private int videoViews;

    @Builder
    public Video(int memberId, String title, int length, int adId) {
        this.title = title;
        this.memberId = memberId;
        this.length = length;
        this.uploadDate = LocalDateTime.now();
        this.totalPlaytime = 0;
        this.videoViews = 0;
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
        this.totalPlaytime = getTotalPlaytime() + playTime;
    }

}