package com.example.week6project.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;


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
    private Enum userRole;

    public void addPoint(int point) {
        this.point += point;
    }

    public void addWinCount(int winCount) {
        this.totalWinCount += winCount;
    }



    public void addProfileImg(String url) {
        this.img = url;
    }
}
