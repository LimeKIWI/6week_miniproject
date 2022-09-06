package com.example.week6project.domain;

import com.example.week6project.domain.results.CounterResult;
import com.example.week6project.domain.results.DiceResult;
import com.example.week6project.domain.results.LottoResult;
import com.example.week6project.domain.results.OddEvenResult;
import com.example.week6project.dto.requestDto.NicknameDuplicateCheckRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {

    @Id
    private String id;

    @Column(nullable = false)
    private String passWord;

    @Column(nullable = false)
    private String nickName;

    @Column
    private int totalWinCount;
    @Column
    private String img;

    @Column
    private int birthDate;

    @Column
    private int point;

    @Column
    private String userRole;

    @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "member")
    private OddEvenResult oddEvenResult;

    @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "member")
    private DiceResult diceResult;

    @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "member")
    private LottoResult lottoResult;

    @OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "member")
    private CounterResult counterResult;

    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "member")
    private List<Lotto> lotto ;

    public void addPoint(int point) {
        this.point += point;
    }

    public void addWinCount(int winCount) {
        this.totalWinCount += winCount;
    }

    public void addProfileImg(String url) {
        this.img = url;
    }

    public void updateNickname(NicknameDuplicateCheckRequestDto requestDto) {
        this.nickName = requestDto.getNickName();
    }
}
