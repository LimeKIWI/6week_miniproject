package com.example.week6project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LottoResultResponseDto {
    private int no; // 회차
    private int[] luckyNum; // 당첨번호
    private int bonusNum; // 보너스 번호
    private int point1st; // 1등 당첨금(예상금)
    private List<String> member1st; // 1등 당첨자
    private int point2nd; // 2등 당첨금(예상금)
    private List<String> member2nd; // 2등 당첨자
    private int point3nd; // 3등 당첨금(예상금)
    private List<String> member3rd; // 3등 당첨자
}
