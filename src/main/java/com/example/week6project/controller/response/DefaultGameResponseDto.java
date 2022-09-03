package com.example.week6project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter

public class DefaultGameResponseDto {
    private String result;
    private int winCount;
    private int getPoint;
    private int nowPoint;
}
