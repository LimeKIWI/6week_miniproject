package com.example.week6project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
// 게임 결과 출력 dto (홀짝, 주사위)
public class OddEvenResponseDto {
    private String result;
    private int winCount;
    private int getPoint;
    private int nowPoint;
}
