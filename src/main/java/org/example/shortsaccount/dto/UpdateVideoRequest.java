package org.example.shortsaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateVideoRequest {
    private String title;
    private int length;
}