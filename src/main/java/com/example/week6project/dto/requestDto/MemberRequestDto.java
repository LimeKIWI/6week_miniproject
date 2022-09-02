package com.example.week6project.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    private String id;

    private String password;

    private String nickname;

    private String birthDate;

   }
