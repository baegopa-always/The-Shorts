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
public class VideoAdvertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "played_ad_id", updatable = false)
    private Long playedAdId;

    @Column(name = "video_id", nullable = false)
    private int videoId;

    @Column(name = "ad_id", nullable = false)
    private int adId;

    @Column(name = "ad_timestamp", columnDefinition = "TIMESTAMP")
    private LocalDateTime adTimestamp;

    @Builder
    public VideoAdvertisement(int videoId, int adId) {
        this.videoId = videoId;
        this.adId = adId;
        this.adTimestamp = LocalDateTime.now();
    }

}