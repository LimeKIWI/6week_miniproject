package com.example.week6project.controller.response.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 최대 클릭 횟수 순위 저장 dto
public class MaxCountResponseDto {
    private int rank;
    private String nickName;
    private int maxCount;
}
