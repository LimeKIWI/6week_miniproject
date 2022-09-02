package com.example.week6project.service;

import com.example.week6project.controller.request.CommentRequestDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.GameType;
import com.example.week6project.domain.comments.OddEvenComment;
import com.example.week6project.repository.CounterCommentRepository;
import com.example.week6project.repository.DiceCommentRepository;
import com.example.week6project.repository.LottoCommentRepository;
import com.example.week6project.repository.OddEvenCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final OddEvenCommentRepository oddEvenCommentRepository;
    private final DiceCommentRepository diceCommentRepository;
    private final LottoCommentRepository lottoCommentRepository;
    private final CounterCommentRepository commentRepository;

    public ResponseDto<?> createComment(CommentRequestDto commentRequestDto, HttpServletRequest request, GameType gameType) {

        String comment = commentRequestDto.getContent();
        switch(gameType.name()) {
            case "ODD_EVEN":
            case "DICE":
            case "LOTTO":
            case "COUNTER":
        }
        return ResponseDto.fail("BAD_REQUEST","댓글 작성 오류");
    }

    public ResponseDto<?> updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request, GameType gameType) {
        return ResponseDto.success("성공");
    }

    public ResponseDto<?> deleteComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request, GameType gameType) {
        return ResponseDto.success("성공");
    }

    public ResponseDto<?> getCommentList(HttpServletRequest request, GameType gameType) {
        return ResponseDto.success("성공");
    }
}
