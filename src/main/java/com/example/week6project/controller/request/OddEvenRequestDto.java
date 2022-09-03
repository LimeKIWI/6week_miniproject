package com.example.week6project.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OddEvenRequestDto {
    private int number; // 홀,짝 선택
    private int point; // 배팅한 포인트
}
