package com.example.week6project.service;

import java.util.Random;

public class LottoService {

    /*
    1등 : 6개 번호 모두 일치
        => 총 포인트 중 4~5등을 제외한 포인트의 75%
    2등 : 5개 번호 일치 + 보너스볼 번호 일치
        => 총 포인트 중 4~5등을 제외한 포인트의 12.5%
    3등 : 5개 번호 일치
        => 총 포인트 중 4~5등을 제외한 포인트의 12.5%
    4등 : 4개 번호 일치
        => 로또 금액 * 50
    5등 : 3개 번호 일치
        => 로또 금액 * 5
     */

    public int[] luckyNum(){
        Random random = new Random();
        int[] luckynum=new int[7];
        luckynum[0]=random.nextInt(45)+1;
        for (int i = 1; i < 7; i++) {
            luckynum[i]=random.nextInt(45)+1;
            for (int j = 0; j < i; j++) {
                if(luckynum[i]==luckynum[j]){
                    i-=1;
                }
            }

        }
        return luckynum;
    }

}
