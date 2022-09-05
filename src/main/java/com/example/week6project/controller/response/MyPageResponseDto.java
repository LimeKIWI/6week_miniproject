package com.example.week6project.controller.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
// 마이페이지 출력 dto
public class MyPageResponseDto {
    private String id;
    private String nickName;
    private int point;
    private int winCountOfOddEven;
    private int winCountOfDice;
    private int winCountOfLotto;
    private int highestCountOfCounter;
}
