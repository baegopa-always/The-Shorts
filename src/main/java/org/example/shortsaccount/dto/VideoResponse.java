package org.example.shortsaccount.dto;

import lombok.Getter;
import org.example.shortsaccount.domain.Video;

import java.time.LocalDateTime;

@Getter
public class VideoResponse {
    private final String title;
    private final int length;
    private final LocalDateTime uploadDate;
    public VideoResponse(Video video) {
        this.title = video.getTitle();
        this.length = video.getLength();
        this.uploadDate = video.getUploadDate();
    }
}