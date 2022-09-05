package com.example.week6project.controller.response.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 승리 횟수 저장 dto
public class WinCountRankResponseDto {
    private int rank;
    private String nickName;
    private int totalWinCount;
}
