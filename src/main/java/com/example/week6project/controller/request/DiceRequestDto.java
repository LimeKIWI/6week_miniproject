package com.example.week6project.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiceRequestDto {
    private int number; // 주사위 눈금 선택
    private int point; // 배팅한 포인트
}
