package org.example.shortsaccount.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopFiveVideoDTO {
    private int videoId;
    private int views;
    private int playtime;
}
