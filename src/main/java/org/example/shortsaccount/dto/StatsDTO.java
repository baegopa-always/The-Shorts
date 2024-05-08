package org.example.shortsaccount.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StatsDTO {
    private int videoId;
    private int statsAmount;
    private LocalDate statsDate;
    private int dailyViewAmount;
    private int dailyAdAmount;
}
