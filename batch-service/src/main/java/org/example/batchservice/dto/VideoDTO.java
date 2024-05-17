package org.example.batchservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class VideoDTO {
    private int videoId;
    private int memberId;
    private String title;
    private LocalDateTime uploadDate;
    private int length;
    private int totalPlaytime;
    private int videoViews;
}
