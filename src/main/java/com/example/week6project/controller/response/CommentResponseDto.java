package com.example.week6project.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
// 댓글 출력 dto
public class CommentResponseDto {
    private Long id;
    private String nickName;
    private String content;
}
