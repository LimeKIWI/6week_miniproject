package com.example.week6project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CounterResponseDto {
    private int nowcount;
    private int maxcount;
    private int getpoint;
    private int nowpoint;
}
