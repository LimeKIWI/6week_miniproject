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
    private String pw;

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

}
