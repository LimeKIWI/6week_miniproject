package com.example.week6project.service;

import com.example.week6project.controller.response.MyPageResponseDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.ImageMapper;
import com.example.week6project.domain.Member;
import com.example.week6project.domain.results.DiceResult;
import com.example.week6project.domain.results.OddEvenResult;
import com.example.week6project.repository.MemberRepository;
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

    // 마이페이지 불러오기
    public ResponseDto<?> getMyPage(HttpServletRequest request) {
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member) chkResponse.getData();

        OddEvenResult oddEvenResult = oddEvenResultRepository.findByMember(member);
        DiceResult diceResult = diceResultRepository.findByMember(member);
       // LottoResult lottoResult = lottoResultRepository.findByMember(member);
       // CounterResult counterResult = counterResultRepository.findByMember(member);

        MyPageResponseDto myPageResponseDto = MyPageResponseDto.builder()
                .id(member.getId())
                .nickName(member.getNickName())
                .point(member.getPoint())
                .winCountOfOddEven(oddEvenResult.getWinCount())
                .winCountOfDice(diceResult.getWinCount())
                .winCountOfLotto(0)
                .highestCountOfCounter(0)
                .build();

        return ResponseDto.success(myPageResponseDto);
    }


    // 프로필 이미지 업로드
    @Transactional
    public ResponseDto<?> profileImageUpload(Long id, MultipartFile multipartFile, HttpServletRequest request) {
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
