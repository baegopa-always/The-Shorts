package org.example.shortsaccount.dto;

import lombok.Getter;
import org.example.shortsaccount.domain.Video;

import java.time.LocalDateTime;

@Getter
public class VideoResponse {
    private final long videoId;
    private final String title;
    private final int length;
    private final LocalDateTime uploadDate;
    private final int memberId;
    private final int playbackTime;
    private final int videoViews;

    public VideoResponse(Video video) {
        this.videoId = video.getVideoId();
        this.title = video.getTitle();
        this.length = video.getLength();
        this.uploadDate = video.getUploadDate();
        this.memberId = video.getMemberId();
        this.playbackTime = video.getPlaybackTime();
        this.videoViews = video.getVideoViews();
    }
}