package com.example.week6project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Lotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int num1;

    @Column
    private int num2;

    @Column
    private int num3;

    @Column
    private int num4;

    @Column
    private int num5;

    @Column
    private int num6;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private long result;


    public void plusOne(){
        this.result+=1;
    }
    public void plusBonus(){
        this.result+=10;
    }

}
