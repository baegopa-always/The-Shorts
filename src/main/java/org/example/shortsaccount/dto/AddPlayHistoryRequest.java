package org.example.shortsaccount.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.shortsaccount.domain.PlayHistory;

@Getter
@Setter
public class AddPlayHistoryRequest {
    private int videoId;
    private int playTime;
    public PlayHistory toEntity() {
        return PlayHistory.builder()
                .videoId(videoId)
                .adViews((int) playTime/300)
                .build();
    }
}