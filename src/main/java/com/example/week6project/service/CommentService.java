package com.example.week6project.service;

import com.example.week6project.security.TokenProvider;
import com.example.week6project.controller.request.CommentRequestDto;
import com.example.week6project.controller.response.CommentResponseDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.GameType;
import com.example.week6project.domain.Member;
import com.example.week6project.domain.comments.CounterComment;
import com.example.week6project.domain.comments.DiceComment;
import com.example.week6project.domain.comments.LottoComment;
import com.example.week6project.domain.comments.OddEvenComment;
import com.example.week6project.repository.comments.CounterCommentRepository;
import com.example.week6project.repository.comments.DiceCommentRepository;
import com.example.week6project.repository.comments.LottoCommentRepository;
import com.example.week6project.repository.comments.OddEvenCommentRepository;
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

    // 댓글 작성
    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto commentRequestDto, HttpServletRequest request, GameType gameType) {
        //로그인확인
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        // 각 게임 타입별 댓글 작성
        switch(gameType.name()) {
            case "ODD_EVEN":
                OddEvenComment oddEvenComment = OddEvenComment.builder()
                        .member(member)
                        .content(commentRequestDto.getContent())
                        .build();
                oddEvenCommentRepository.save(oddEvenComment);
                return ResponseDto.success(CommentResponseDto.builder()
                        .nickName(oddEvenComment.getMember().getNickName())
                        .content(commentRequestDto.getContent())
                        .build());
            case "DICE":
                DiceComment diceComment = DiceComment.builder()
                        .member(member)
                        .content(commentRequestDto.getContent())
                        .build();
                diceCommentRepository.save(diceComment);
                return ResponseDto.success(CommentResponseDto.builder()
                        .nickName(diceComment.getMember().getNickName())
                        .content(commentRequestDto.getContent())
                        .build());
            case "LOTTO":
                LottoComment lottoComment = LottoComment.builder()
                        .member(member)
                        .content(commentRequestDto.getContent())
                        .build();
                lottoCommentRepository.save(lottoComment);
                return ResponseDto.success(CommentResponseDto.builder()
                        .nickName(lottoComment.getMember().getNickName())
                        .content(commentRequestDto.getContent())
                        .build());
            case "COUNTER":
                CounterComment counterComment = CounterComment.builder()
                        .member(member)
                        .content(commentRequestDto.getContent())
                        .build();
                counterCommentRepository.save(counterComment);
                return ResponseDto.success(CommentResponseDto.builder()
                        .nickName(counterComment.getMember().getNickName())
                        .content(commentRequestDto.getContent())
                        .build());
        }
        // 타입불일치 (실제로 여기까지 오진 않음, enum범위를 넘어섬)
        return ResponseDto.fail("BAD_REQUEST_C","댓글 타입 오류");
    }

    // 댓글 수정
    @Transactional
    public ResponseDto<?> updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request, GameType gameType) {
        // 로그인 확인
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        // 게임 타입 확인
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

    // 댓글 삭제
    @Transactional
    public ResponseDto<?> deleteComment(Long id, HttpServletRequest request, GameType gameType) {
        // 로그인 확인
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        // 게임 타입 확인
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

    // 댓글 불러오기
    @Transactional
    public ResponseDto<?> getCommentList(HttpServletRequest request, GameType gameType) {
        // 로그인 확인
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;

        // 댓글 리스트가 저장될 리스트객체 생성
        List<CommentResponseDto> commentResponseList = new ArrayList<>();
        // 게임 타입 확인
        switch(gameType.name()) {

            case "ODD_EVEN":
                List<OddEvenComment> list1 = oddEvenCommentRepository.findAllByOrderByModifiedAtDesc();
                for(OddEvenComment comment : list1) {
                   CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                           .id(comment.getId())
                           .nickName(comment.getMember().getNickName())
                           .content(comment.getContent())
                           .build();
                   commentResponseList.add(commentResponseDto);
                }
                return ResponseDto.success(commentResponseList);

            case "DICE":
                List<DiceComment> list2 = diceCommentRepository.findAllByOrderByModifiedAtDesc();
                for(DiceComment comment : list2) {
                    CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                            .id(comment.getId())
                            .nickName(comment.getMember().getNickName())
                            .content(comment.getContent())
                            .build();
                    commentResponseList.add(commentResponseDto);
                }
                return ResponseDto.success(commentResponseList);

            case "LOTTO":
                List<LottoComment> list3 = lottoCommentRepository.findAllByOrderByModifiedAtDesc();
                for(LottoComment comment : list3) {
                    CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                            .id(comment.getId())
                            .nickName(comment.getMember().getNickName())
                            .content(comment.getContent())
                            .build();
                    commentResponseList.add(commentResponseDto);
                }
                return ResponseDto.success(commentResponseList);

            case "COUNTER":
                List<CounterComment> list4 = counterCommentRepository.findAllByOrderByModifiedAtDesc();
                for(CounterComment comment : list4) {
                    CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                            .id(comment.getId())
                            .nickName(comment.getMember().getNickName())
                            .content(comment.getContent())
                            .build();
                    commentResponseList.add(commentResponseDto);
                }
                return ResponseDto.success(commentResponseList);
        }

        return ResponseDto.fail("BAD_REQUEST_R","댓글 타입 오류");
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
