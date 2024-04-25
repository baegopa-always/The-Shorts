package org.example.shortsaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.shortsaccount.domain.Video;

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
                .memberId(memberId)
                .build();
    }
}