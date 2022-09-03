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
public class CounterResult {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long Id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private int maxCount;

    @Column
    private int playCount;

    @Column
    private int earnPoint;

    public void result(int earnPoint, int maxCount) {
        this.playCount++;
        this.earnPoint += earnPoint;
        if(maxCount > this.maxCount)
            this.maxCount = maxCount;
    }
}
