package org.example.shortsaccount.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayHistoryDTO {
    private int videoId;
    private int playTime;
    private int memberId;
    private int lastWatchTime;
}