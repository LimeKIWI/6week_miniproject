package com.example.week6project.controller;

import com.example.week6project.controller.request.CommentRequestDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.GameType;
import com.example.week6project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @RequestMapping(value = "/api/comment", method = RequestMethod.POST)
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto commentRequestDto, @RequestParam("gameId") int gameType, HttpServletRequest request) {
        return commentService.createComment(commentRequestDto, request, GameType.values()[gameType-1]);
    }

    // 댓글 수정
    @RequestMapping(value = "/api/comment", method = RequestMethod.PATCH)
    public ResponseDto<?> updateComment(@RequestBody CommentRequestDto commentRequestDto, @RequestParam("gameId") int gameType, @RequestParam("Id") Long commentId,  HttpServletRequest request) {
        return commentService.updateComment(commentId, commentRequestDto, request, GameType.values()[gameType-1]);
    }
    // 댓글 삭제
    @RequestMapping(value = "/api/comment", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteComment(@RequestParam("gameId") int gameType, @RequestParam("Id") Long commentId, HttpServletRequest request) {
        return commentService.deleteComment(commentId, request, GameType.values()[gameType-1]);
    }

    // 댓글 보기
    @RequestMapping(value = "/api/comment", method = RequestMethod.GET)
    public ResponseDto<?> getCommentList(@RequestParam("gameId") int gameType, HttpServletRequest request) {
        return commentService.getCommentList(request, GameType.values()[gameType-1]);
    }
}
