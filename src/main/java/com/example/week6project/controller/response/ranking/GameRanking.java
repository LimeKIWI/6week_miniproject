package com.example.week6project.controller.response.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRanking {
    private String gameTitle;
    private List<WinCountRankResponseDto> WinCountList;
    private List<EarnPointResponseDto> earnPointList;
}
