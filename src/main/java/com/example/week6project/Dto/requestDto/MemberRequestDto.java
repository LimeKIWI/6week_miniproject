package com.example.week6project.Dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    private String id;

    private String pw;

    private String nickname;

    private String birthDate;

   }
