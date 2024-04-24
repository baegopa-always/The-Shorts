package org.example.shortsaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.shortsaccount.domain.Video;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddVideoRequest {
    private String title;
    private int length;
    private int memberId;
    public Video toEntity() {
        return Video.builder()
                .title(title)
                .length(length)
                .uploadDate(LocalDateTime.now())
                .memberId(memberId)
                .build();
    }
}