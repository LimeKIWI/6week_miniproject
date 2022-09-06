package com.example.week6project.domain;

import com.example.week6project.dto.requestDto.NicknameDuplicateCheckRequestDto;
import lombok.*;

import javax.persistence.*;


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
