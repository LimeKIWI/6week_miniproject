package com.example.week6project.service;

import com.example.week6project.controller.response.MyPageResponseDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.ImageMapper;
import com.example.week6project.domain.Member;
import com.example.week6project.domain.comments.Comment;
import com.example.week6project.domain.results.CounterResult;
import com.example.week6project.domain.results.DiceResult;
import com.example.week6project.domain.results.LottoResult;
import com.example.week6project.domain.results.OddEvenResult;
import com.example.week6project.dto.TokenDto;
import com.example.week6project.dto.requestDto.NicknameDuplicateCheckRequestDto;
import com.example.week6project.repository.MemberRepository;
import com.example.week6project.repository.RefreshTokenRepository;
import com.example.week6project.repository.comments.CommentRepository;
import com.example.week6project.repository.results.CounterResultRepository;
import com.example.week6project.repository.results.DiceResultRepository;
import com.example.week6project.repository.results.LottoResultRepository;
import com.example.week6project.repository.results.OddEvenResultRepository;
import com.example.week6project.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TokenProvider tokenProvider;
    private final AmazonS3Service amazonS3Service;
    private final OddEvenResultRepository oddEvenResultRepository;
    private final MemberRepository memberRepository;
    private final DiceResultRepository diceResultRepository;
    private final CounterResultRepository counterResultRepository;
    private final LottoResultRepository lottoResultRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final CommentRepository commentRepository;

    // 마이페이지 불러오기
    public ResponseDto<?> getMyPage(HttpServletRequest request) {
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        OddEvenResult oddEvenResult = oddEvenResultRepository.findByMember(member);
        DiceResult diceResult = diceResultRepository.findByMember(member);
        LottoResult lottoResult = lottoResultRepository.findByMember(member);
        CounterResult counterResult = counterResultRepository.findByMember(member);

        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                .id(member.getId())
                .nickName(member.getNickName())
                .point(member.getPoint())
                .winCountOfOddEven(oddEvenResult.getWinCount())
                .winCountOfDice(diceResult.getWinCount())
                .earnPointOfLotto(lottoResult.getEarnPoint())
                .highestCountOfCounter(counterResult.getMaxCount())
                .build();

        return ResponseDto.success(myPageResponseDto);
    }


    // 프로필 이미지 업로드
    @Transactional
    public ResponseDto<?> profileImageUpload(MultipartFile multipartFile, HttpServletRequest request) {
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        Member updateMember = memberRepository.findById(member.getId()).get();

        ResponseDto<?> result = amazonS3Service.uploadFile(multipartFile);
        if(!result.isSuccess())
            return result;
        ImageMapper imageMapper = (ImageMapper)result.getData();

        updateMember.addProfileImg(imageMapper.getUrl());

        return ResponseDto.success(updateMember.getImg());
    }



    // 유저닉네임 업데이트
    @Transactional
    public ResponseDto<?> updateInfo(NicknameDuplicateCheckRequestDto nicknameRequestDto, HttpServletRequest request, HttpServletResponse response) {
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        Member updateMember = memberRepository.findById(member.getId()).get();

        OddEvenResult oddEvenResult = oddEvenResultRepository.findByMember(updateMember);
        DiceResult diceResult = diceResultRepository.findByMember(updateMember);
        LottoResult lottoResult = lottoResultRepository.findByMember(updateMember);
        CounterResult counterResult = counterResultRepository.findByMember(updateMember);

        updateMember.updateNickname(nicknameRequestDto);

        // 기존 토큰 삭제

        refreshTokenRepository.delete(refreshTokenRepository.findByMember(member).get());

        // 토큰 재생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);

        //헤더에 반환 to FE
        response.addHeader("Authorization","Bearer "+tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        return ResponseDto.success(MyPageResponseDto.builder()
                .id(updateMember.getId())
                .nickName(updateMember.getNickName())
                .point(updateMember.getPoint())
                .winCountOfOddEven(oddEvenResult.getWinCount())
                .winCountOfDice(diceResult.getWinCount())
                .earnPointOfLotto(lottoResult.getEarnPoint())
                .highestCountOfCounter(counterResult.getMaxCount())
                .build());
    }

    // 회원탈퇴
    @Transactional
    public ResponseDto<?> withDrawal(HttpServletRequest request) {
        // 회원정보 받아오기
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        Member getMember = memberRepository.findById(member.getId()).get();                 // 실제 회원 찾기
        Optional<?> updateMember = memberRepository.findByNickName("탈퇴한 사용자입니다.");    // temp회원 찾기

        List<Comment> comments = commentRepository.findAllByMember(getMember);              // 회원이 쓴 댓글 찾기
        for(Comment comment : comments) {
            comment.setMember((Member) updateMember.get());         //  temp회원으로 댓글 member 교체
        }

        tokenProvider.deleteRefreshToken(getMember);
        memberRepository.deleteById(getMember.getId());             // 회원 삭제
        return ResponseDto.success("탈퇴 성공");
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
