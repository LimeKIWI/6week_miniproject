package com.example.week6project.domain.results;

import com.example.week6project.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LottoResult {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long Id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private int coutn1st;
    private int coutn2nd;
    private int coutn3rd;
    private int coutn4th;
    private int coutn5th;

    @Column
    private int playCount;

    @Column
    private int earnPoint;

    public void result(int result, int earnPoint) {
        this.playCount++;
        this.earnPoint += earnPoint;
        if(result==1){
            this.coutn1st+=1;
        }
        if(result==2){
            this.coutn2nd+=1;
        }
        if(result==3){
            this.coutn3rd+=1;
        }
        if(result==4){
            this.coutn4th+=1;
        }
        if(result==5){
            this.coutn5th+=1;
        }
    }
}
