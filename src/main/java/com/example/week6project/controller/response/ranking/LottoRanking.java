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
// 로또 랭킹 출력 dto
public class LottoRanking {
    private String gameTitle;
    private List<EarnPointResponseDto> earnPointList;
}
