package org.example.shortsaccount.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
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
    private Long id;

    @Column(name = "member_id", nullable = false)
    private int memberId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "upload_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime uploadDate;

    @Column(name = "length")
    private int length;

    @Builder
    public Video(int memberId, String title, int length, LocalDateTime uploadDate) {
        this.title = title;
        this.memberId = memberId;
        this.length = length;
        this.uploadDate = uploadDate;
    }

    public void update(String title, int length) {
        this.title = title;
        this.length = length;
    }
}