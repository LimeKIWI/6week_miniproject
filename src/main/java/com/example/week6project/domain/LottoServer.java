package com.example.week6project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LottoServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int point;

    @Column
    private int luckyNum1;
    @Column
    private int luckyNum2;
    @Column
    private int luckyNum3;
    @Column
    private int luckyNum4;
    @Column
    private int luckyNum5;
    @Column
    private int luckyNum6;

    @Column
    private int point1st;

    @Column
    private int point2nd;

    @Column
    private int point3rd;

    public void plusPoint(int point){
        this.point+=point;
    }



    public void setLuckyNum(int[] luckyNum) {
        this.luckyNum1=luckyNum[0];
        this.luckyNum2=luckyNum[1];
        this.luckyNum3=luckyNum[2];
        this.luckyNum4=luckyNum[3];
        this.luckyNum5=luckyNum[4];
        this.luckyNum6=luckyNum[5];

    }
}
