package org.example.shortsaccount.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TopFiveVideoDTO {
    private int videoId;
    private int views;
    private int playtime;
}
