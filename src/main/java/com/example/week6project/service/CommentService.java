package com.example.week6project.service;

import com.example.week6project.Security.TokenProvider;
import com.example.week6project.controller.request.CommentRequestDto;
import com.example.week6project.controller.response.CommentResponseDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.GameType;
import com.example.week6project.domain.Member;
import com.example.week6project.domain.comments.CounterComment;
import com.example.week6project.domain.comments.DiceComment;
import com.example.week6project.domain.comments.LottoComment;
import com.example.week6project.domain.comments.OddEvenComment;
import com.example.week6project.repository.CounterCommentRepository;
import com.example.week6project.repository.DiceCommentRepository;
import com.example.week6project.repository.LottoCommentRepository;
import com.example.week6project.repository.OddEvenCommentRepository;
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

    private final OddEvenCommentRepository oddEvenCommentRepository;
    private final DiceCommentRepository diceCommentRepository;
    private final LottoCommentRepository lottoCommentRepository;
    private final CounterCommentRepository counterCommentRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto commentRequestDto, HttpServletRequest request, GameType gameType) {
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        switch(gameType.name()) {
            case "ODD_EVEN":
                OddEvenComment oddEvenComment = OddEvenComment.builder()
                        .member(member)
                        .content(commentRequestDto.getContent())
                        .build();
                oddEvenCommentRepository.save(oddEvenComment);
                return ResponseDto.success(CommentResponseDto.builder()
                        .nickName(oddEvenComment.getMember().getNickname())
                        .content(commentRequestDto.getContent())
                        .build());
            case "DICE":
                DiceComment diceComment = DiceComment.builder()
                        .member(member)
                        .content(commentRequestDto.getContent())
                        .build();
                diceCommentRepository.save(diceComment);
                return ResponseDto.success(CommentResponseDto.builder()
                        .nickName(diceComment.getMember().getNickname())
                        .content(commentRequestDto.getContent())
                        .build());
            case "LOTTO":
                LottoComment lottoComment = LottoComment.builder()
                        .member(member)
                        .content(commentRequestDto.getContent())
                        .build();
                lottoCommentRepository.save(lottoComment);
                return ResponseDto.success(CommentResponseDto.builder()
                        .nickName(lottoComment.getMember().getNickname())
                        .content(commentRequestDto.getContent())
                        .build());
            case "COUNTER":
                CounterComment counterComment = CounterComment.builder()
                        .member(member)
                        .content(commentRequestDto.getContent())
                        .build();
                counterCommentRepository.save(counterComment);
                return ResponseDto.success(CommentResponseDto.builder()
                        .nickName(counterComment.getMember().getNickname())
                        .content(commentRequestDto.getContent())
                        .build());
        }

        return ResponseDto.fail("BAD_REQUEST_C","댓글 타입 오류");
    }


    @Transactional
    public ResponseDto<?> updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request, GameType gameType) {
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        switch(gameType.name()) {

            case "ODD_EVEN":
                Optional<OddEvenComment> comment1 = oddEvenCommentRepository.findById(id);
                if(comment1.isEmpty())
                    return ResponseDto.fail("COMMENT_NOT_FOUND", "코멘트를 찾을 수 없습니다.");
                OddEvenComment oddEvenComment = comment1.get();
                if (!oddEvenComment.validateMember(member))
                    return ResponseDto.fail("AUTHOR_NOT_MATCHED", "작성자가 아닙니다.");
                oddEvenComment.update(commentRequestDto);
                return ResponseDto.success("수정완료");

            case "DICE":
                Optional<DiceComment> comment2 = diceCommentRepository.findById(id);
                if(comment2.isEmpty())
                    return ResponseDto.fail("COMMENT_NOT_FOUND", "코멘트를 찾을 수 없습니다.");
                DiceComment diceComment = comment2.get();
                if (!diceComment.validateMember(member))
                    return ResponseDto.fail("AUTHOR_NOT_MATCHED", "작성자가 아닙니다.");
                diceComment.update(commentRequestDto);
                return ResponseDto.success("수정완료");

            case "LOTTO":
                Optional<LottoComment> comment3 = lottoCommentRepository.findById(id);
                if(comment3.isEmpty())
                    return ResponseDto.fail("COMMENT_NOT_FOUND", "코멘트를 찾을 수 없습니다.");
                LottoComment lottoComment = comment3.get();
                if (!lottoComment.validateMember(member))
                    return ResponseDto.fail("AUTHOR_NOT_MATCHED", "작성자가 아닙니다.");
                lottoComment.update(commentRequestDto);
                return ResponseDto.success("수정완료");

            case "COUNTER":
                Optional<CounterComment> comment4 = counterCommentRepository.findById(id);
                if(comment4.isEmpty())
                    return ResponseDto.fail("COMMENT_NOT_FOUND", "코멘트를 찾을 수 없습니다.");
                CounterComment counterComment = comment4.get();
                if (!counterComment.validateMember(member))
                    return ResponseDto.fail("AUTHOR_NOT_MATCHED", "작성자가 아닙니다.");
                counterComment.update(commentRequestDto);
                return ResponseDto.success("수정완료");
        }

        return ResponseDto.fail("BAD_REQUEST_U","댓글 타입 오류");
    }

    @Transactional
    public ResponseDto<?> deleteComment(Long id, HttpServletRequest request, GameType gameType) {
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        switch(gameType.name()) {

            case "ODD_EVEN":
                Optional<OddEvenComment> comment1 = oddEvenCommentRepository.findById(id);
                if(comment1.isEmpty())
                    return ResponseDto.fail("COMMENT_NOT_FOUND", "코멘트를 찾을 수 없습니다.");
                OddEvenComment oddEvenComment = comment1.get();
                if (!oddEvenComment.validateMember(member))
                    return ResponseDto.fail("AUTHOR_NOT_MATCHED", "작성자가 아닙니다.");

                oddEvenCommentRepository.deleteById(id);
                return ResponseDto.success("삭제완료");

            case "DICE":
                Optional<DiceComment> comment2 = diceCommentRepository.findById(id);
                if(comment2.isEmpty())
                    return ResponseDto.fail("COMMENT_NOT_FOUND", "코멘트를 찾을 수 없습니다.");
                DiceComment diceComment = comment2.get();
                if (!diceComment.validateMember(member))
                    return ResponseDto.fail("AUTHOR_NOT_MATCHED", "작성자가 아닙니다.");

                diceCommentRepository.deleteById(id);
                return ResponseDto.success("삭제완료");

            case "LOTTO":
                Optional<LottoComment> comment3 = lottoCommentRepository.findById(id);
                if(comment3.isEmpty())
                    return ResponseDto.fail("COMMENT_NOT_FOUND", "코멘트를 찾을 수 없습니다.");
                LottoComment lottoComment = comment3.get();
                if (!lottoComment.validateMember(member))
                    return ResponseDto.fail("AUTHOR_NOT_MATCHED", "작성자가 아닙니다.");

                lottoCommentRepository.deleteById(id);
                return ResponseDto.success("삭제완료");

            case "COUNTER":
                Optional<CounterComment> comment4 = counterCommentRepository.findById(id);
                if(comment4.isEmpty())
                    return ResponseDto.fail("COMMENT_NOT_FOUND", "코멘트를 찾을 수 없습니다.");
                CounterComment counterComment = comment4.get();
                if (!counterComment.validateMember(member))
                    return ResponseDto.fail("AUTHOR_NOT_MATCHED", "작성자가 아닙니다.");

                counterCommentRepository.deleteById(id);
                return ResponseDto.success("삭제완료");
        }

        return ResponseDto.fail("BAD_REQUEST_D","댓글 타입 오류");
    }

    @Transactional
    public ResponseDto<?> getCommentList(HttpServletRequest request, GameType gameType) {
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;

        List<CommentResponseDto> commentResponseList = new ArrayList<>();
        switch(gameType.name()) {

            case "ODD_EVEN":
                List<OddEvenComment> list1 = oddEvenCommentRepository.findAllByOrderByModifiedAtDesc();
                for(OddEvenComment comment : list1) {
                   CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                           .nickName(comment.getMember().getNickname())
                           .content(comment.getContent())
                           .build();
                   commentResponseList.add(commentResponseDto);
                }
                return ResponseDto.success(commentResponseList);

            case "DICE":
                List<DiceComment> list2 = diceCommentRepository.findAllByOrderByModifiedAtDesc();
                for(DiceComment comment : list2) {
                    CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                            .nickName(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .build();
                    commentResponseList.add(commentResponseDto);
                }
                return ResponseDto.success(commentResponseList);

            case "LOTTO":
                List<LottoComment> list3 = lottoCommentRepository.findAllByOrderByModifiedAtDesc();
                for(LottoComment comment : list3) {
                    CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                            .nickName(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .build();
                    commentResponseList.add(commentResponseDto);
                }
                return ResponseDto.success(commentResponseList);

            case "COUNTER":
                List<CounterComment> list4 = counterCommentRepository.findAllByOrderByModifiedAtDesc();
                for(CounterComment comment : list4) {
                    CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                            .nickName(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .build();
                    commentResponseList.add(commentResponseDto);
                }
                return ResponseDto.success(commentResponseList);
        }

        return ResponseDto.fail("BAD_REQUEST_R","댓글 타입 오류");
    }

    private ResponseDto<?> validateCheck(HttpServletRequest request) {
        if(null == request.getHeader("Refresh-Token") || null == request.getHeader("Authorization")) {
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
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
