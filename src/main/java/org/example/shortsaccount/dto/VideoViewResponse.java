package org.example.shortsaccount.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.shortsaccount.domain.Video;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class VideoViewResponse {
    private Long id;
    private String title;
    private int length;
    private LocalDateTime uploadDate;

    public VideoViewResponse(Video video) {
        this.id = video.getVideoId();
        this.title = video.getTitle();
        this.length = video.getLength();
        this.uploadDate = video.getUploadDate();
    }
}