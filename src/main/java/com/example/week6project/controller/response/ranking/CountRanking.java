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
// 카운트게임 랭킹 출력 dto
public class CountRanking {
    private String gameTitle;
    private List<MaxCountResponseDto> maxCountList;
}
