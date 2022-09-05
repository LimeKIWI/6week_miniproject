package com.example.week6project.controller.response.ranking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 벌어들인 포인트 순위 저장 dto
public class EarnPointResponseDto {
    private int rank;
    private String nickName;
    private int earnPoint;
}
