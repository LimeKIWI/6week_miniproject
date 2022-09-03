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
public class OddEvenResult {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long Id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private int winCount;

    @Column
    private int playCount;

    @Column
    private int earnPoint;

    public void result(int winCount, int earnPoint) {
        this.playCount++;
        this.winCount += winCount;
        this.earnPoint += earnPoint;
    }
}
