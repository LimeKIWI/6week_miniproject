package com.example.week6project.service;

import com.example.week6project.domain.comments.*;
import com.example.week6project.repository.comments.*;
import com.example.week6project.security.TokenProvider;
import com.example.week6project.controller.request.CommentRequestDto;
import com.example.week6project.controller.response.CommentResponseDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.GameType;
import com.example.week6project.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TokenProvider tokenProvider;

    // 댓글 작성
    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto commentRequestDto, HttpServletRequest request, GameType gameType) {
        //로그인확인
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        Comment comment = Comment.builder()
                .member(member)
                .content(commentRequestDto.getContent())
                .gameTypeEnum(gameType)
                .build();
        commentRepository.save(comment);

        return ResponseDto.success(CommentResponseDto.builder()
                .id(comment.getId())
                .nickName(comment.getMember().getNickName())
                .content(commentRequestDto.getContent())
                .build());

    }

    // 댓글 수정
    @Transactional
    public ResponseDto<?> updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        // 로그인 확인
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        Optional<Comment> getComment = commentRepository.findById(id);
        if(getComment.isEmpty())
            return ResponseDto.fail("COMMENT_NOT_FOUND", "코멘트를 찾을 수 없습니다.");
        Comment comment = getComment.get();
        if(!comment.validateMember(member))
            return ResponseDto.fail("AUTHOR_NOT_MATCHED", "작성자가 아닙니다.");

        comment.update(commentRequestDto);
        return ResponseDto.success("수정완료");
    }

    // 댓글 삭제
    @Transactional
    public ResponseDto<?> deleteComment(Long id, HttpServletRequest request) {
        // 로그인 확인
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        Optional<Comment> getComment = commentRepository.findById(id);
        if(getComment.isEmpty())
            return ResponseDto.fail("COMMENT_NOT_FOUND", "코멘트를 찾을 수 없습니다.");
        Comment comment = getComment.get();
        if(!comment.validateMember(member))
            return ResponseDto.fail("AUTHOR_NOT_MATCHED", "작성자가 아닙니다.");

        commentRepository.deleteById(id);
        return ResponseDto.success("삭제완료");
    }

    // 댓글 불러오기
    @Transactional
    public ResponseDto<?> getCommentList(HttpServletRequest request, GameType gameType) {
        // 로그인 확인
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;

        // 댓글 리스트가 저장될 리스트객체 생성
        List<CommentResponseDto> commentResponseList = new ArrayList<>();
        List<Comment> list = commentRepository.findAllByGameTypeEnumOrderByModifiedAtDesc(gameType);
        for(Comment comment : list) {
            CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                    .id(comment.getId())
                    .nickName(comment.getMember().getNickName())
                    .content(comment.getContent())
                    .build();
            commentResponseList.add(commentResponseDto);
        }
        return ResponseDto.success(commentResponseList);
    }

    private ResponseDto<?> validateCheck(HttpServletRequest request) {
        if(null == request.getHeader("RefreshToken") || null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }
        Member member = validateMember(request);
        if(null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        return ResponseDto.success(member);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
