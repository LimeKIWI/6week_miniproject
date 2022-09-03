package com.example.week6project.service;

import com.example.week6project.controller.request.DiceRequestDto;
import com.example.week6project.controller.request.OddEvenRequestDto;
import com.example.week6project.controller.response.DefaultGameResponseDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.Member;
import com.example.week6project.domain.results.DiceResult;
import com.example.week6project.domain.results.OddEvenResult;
import com.example.week6project.repository.MemberRepository;
import com.example.week6project.repository.results.DiceResultRepository;
import com.example.week6project.repository.results.OddEvenResultRepository;
import com.example.week6project.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    // 배율 설정 (추후 ENUM으로 구현하여 관리자 페이지에서 배율 설정 할 수 있게 기능 구현 ?)
    static final int ODDEVEN_MAGNIFICATION = 2; // 홀짝 배율
    static final int DICE_MAGNIFICATION = 8;    // 주사위 배율

    private final TokenProvider tokenProvider;
    private final OddEvenResultRepository oddEvenResultRepository;
    private final DiceResultRepository diceResultRepository;
    private final MemberRepository memberRepository;

    // 홀,짝 게임
    @Transactional
    public ResponseDto<?> runOddEven(OddEvenRequestDto oddEvenRequestDto, HttpServletRequest request) {
        // 유저 로그인 체크
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member)chkResponse.getData();
        // 유저 테이블에서 유저객체 가져오기
        Member updateMember = memberRepository.findById(member.getId()).get();


        if (oddEvenRequestDto.getNumber() >= 2 || oddEvenRequestDto.getNumber() < 0)
            return ResponseDto.fail("BAD_REQUEST","홀(1) 짝(0)을 걸어주세요");


        // 베팅포인트와 소지포인트 체크
        int bettingPoint = oddEvenRequestDto.getPoint();
        if(bettingPoint <= 0 || updateMember.getPoint() < bettingPoint)
            return ResponseDto.fail("BET_POINT_ERR","소지포인트보다 많이 배팅하셨거나 배팅 포인트가 0입니다");
        if(updateMember.getPoint() <= 0)
            return ResponseDto.fail("ZERO_POINT","모든 포인트를 잃으셨습니다");
        updateMember.addPoint((bettingPoint*-1));   // 배팅한 포인트 차감


        // 유저결산테이블 확인
        Optional<OddEvenResult> findResult = oddEvenResultRepository.findByMember(member);
        if (findResult.isEmpty())
            return ResponseDto.fail("NOT_FOUND_INFO_ODDEVEN", "유저 결산정보 에러");
        OddEvenResult oddEvenResult = findResult.get();


        // 게임시작
        int winCount = 0, point = 0;
        String result = "실패";
        int generateNum = (int)Math.floor(Math.random()*2);
        if(generateNum == oddEvenRequestDto.getNumber()) {
            result = "성공";
            winCount = 1;
            point = bettingPoint* ODDEVEN_MAGNIFICATION;
            updateMember.addPoint(point);       // 이겼다면 포인트 추가
        }
        oddEvenResult.result(winCount,point);   // 포인트및 결과 저장


        // 게임결과 출력
        DefaultGameResponseDto defaultGameResponseDto = DefaultGameResponseDto.builder()
                .result(result)
                .winCount(oddEvenResult.getWinCount())
                .getPoint(point)
                .nowPoint(updateMember.getPoint())
                .build();

        return ResponseDto.success(defaultGameResponseDto);
    }


    // 주사위 게임
    @Transactional
    public ResponseDto<?> runDice(DiceRequestDto diceRequestDto, HttpServletRequest request) {
        // 유저 로그인 체크
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;
        Member member = (Member)chkResponse.getData();
        // 유저 테이블에서 유저객체 가져오기
        Member updateMember = memberRepository.findById(member.getId()).get();


        if (diceRequestDto.getNumber() <= 0 || diceRequestDto.getNumber() > 6)
            return ResponseDto.fail("BAD_REQUEST","주사위 눈금 만큼을 걸어주세요(1~6)");


        // 베팅포인트와 소지포인트 체크
        int bettingPoint = diceRequestDto.getPoint();
        if(bettingPoint <= 0 || updateMember.getPoint() < bettingPoint)
            return ResponseDto.fail("BET_POINT_ERR","소지포인트보다 많이 배팅하셨거나 배팅 포인트가 0입니다");
        if(updateMember.getPoint() <= 0)
            return ResponseDto.fail("ZERO_POINT","모든 포인트를 잃으셨습니다");
        updateMember.addPoint((bettingPoint*-1));   // 배팅한 포인트 차감


        // 유저결산테이블 확인
        Optional<DiceResult> findResult = diceResultRepository.findByMember(member);
        if (findResult.isEmpty())
            return ResponseDto.fail("NOT_FOUND_INFO_ODDEVEN", "유저 결산정보 에러");
        DiceResult diceResult = findResult.get();


        // 게임 시작
        int winCount = 0, point = 0;
        String result = "실패";
        int generateNum = (int)Math.floor(Math.random()*6)+1;
        if(generateNum == diceRequestDto.getNumber()) {
            result = "성공";
            winCount = 1;
            point = bettingPoint* DICE_MAGNIFICATION;
            updateMember.addPoint(point);       // 이겼다면 포인트 추가
        }
        diceResult.result(winCount,point);   // 포인트및 결과 저장

        // 게임결과 출력
        DefaultGameResponseDto defaultGameResponseDto = DefaultGameResponseDto.builder()
                .result(result)
                .winCount(diceResult.getWinCount())
                .getPoint(point)
                .nowPoint(updateMember.getPoint())
                .build();

        return ResponseDto.success(defaultGameResponseDto);

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
