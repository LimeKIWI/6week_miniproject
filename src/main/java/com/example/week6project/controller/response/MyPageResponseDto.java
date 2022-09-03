package com.example.week6project.controller.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MyPageResponseDto {
    private String id;
    private String nickName;
    private int point;
    private int winCountOfOddEven;
    private int winCountOfDice;
    private int winCountOfLotto;
    private int highestCountOfCounter;
}
