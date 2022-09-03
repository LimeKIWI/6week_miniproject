package com.example.week6project.domain;

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
    private String password;

    @Column(nullable = false)
    private String nickname;


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

    public void addProfileImg(String url) {
        this.img = url;
    }
}
