package com.example.week6project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LottoMyResultResponseDto<R> {
    private int no; // 회차
    private R luckyNum; // 당첨번호
    private int bonusNum; // 보너스 번호
    private int[] MyNum; // 내 번호
    private R rank; // 등수
    private int earnPoint; // 당첨금



}
