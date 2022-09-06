package com.example.week6project.service;

import com.example.week6project.controller.response.ranking.*;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.Member;
import com.example.week6project.domain.results.CounterResult;
import com.example.week6project.domain.results.DiceResult;
import com.example.week6project.domain.results.LottoResult;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final MemberRepository memberRepository;
    private final OddEvenResultRepository oddEvenResultRepository;
    private final DiceResultRepository diceResultRepository;
    private final CounterResultRepository counterResultRepository;
    private final LottoResultRepository lottoResultRepository;
    private final TokenProvider tokenProvider;

    // 전체랭킹
    public ResponseDto<?> getTotalRanking(HttpServletRequest request) {
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;

        // 전체 멤버리스트를 이긴횟수 내림차순으로 가져오기
        List<Member> memberWinList = memberRepository.findAllByOrderByTotalWinCountDesc();
        List<WinCountRankResponseDto> countDtolist = new ArrayList<>();
        int count = 0;
        for(int i = 0, n = memberWinList.size(); i < n && count < 30; i++) {
            if(memberWinList.get(i).getUserRole() != null) {
                WinCountRankResponseDto winCountRankResponseDto = WinCountRankResponseDto.builder()     // 출력양식 dto
                        .rank(count + 1)                                                                      // 순위
                        .nickName(memberWinList.get(i).getNickName())                                   // 닉네임
                        .totalWinCount(memberWinList.get(i).getTotalWinCount())                         // 이긴횟수
                        .build();
                countDtolist.add(winCountRankResponseDto);
                count++;
            }
        }
        // 전체 맴버리스트를 소지포인트 내림차순으로 가져오기
        count = 0;
        List<Member> memberPointList = memberRepository.findAllByOrderByPointDesc();
        List<PointRankResponseDto> pointDtoList = new ArrayList<>();
        for(int i = 0, n = memberPointList.size(); i < n && count < 30; i++) {
            if(memberPointList.get(i).getUserRole() != null) {
                PointRankResponseDto pointRankResponseDto = PointRankResponseDto.builder()              // 출력양식 dto
                        .rank(count + 1)                                                                      // 순위
                        .nickName(memberPointList.get(i).getNickName())                                 // 닉네임
                        .totalPoint(memberPointList.get(i).getPoint())                                  // 소지포인트
                        .build();
                pointDtoList.add(pointRankResponseDto);
                count++;
            }
        }

        return ResponseDto.success(TotalRanking.builder()
                        .totalWinCountList(countDtolist)
                        .totalPointList(pointDtoList)
                        .build());
    }

    // 게임별 랭킹
    public ResponseDto<?> getGameRanking(Long id, HttpServletRequest request) {
        ResponseDto<?> chkResponse =  validateCheck(request);
        if(!chkResponse.isSuccess())
            return chkResponse;

        // 홀짝게임
        if(id == 1) {
            // 이긴횟수별
            List<OddEvenResult> oddWinList = oddEvenResultRepository.findByOrderByWinCountDesc();
            List<WinCountRankResponseDto> oddWinListDto = new ArrayList<>();
            for(int i = 0, n = oddWinList.size(); i < n && i < 30; i++) {
                WinCountRankResponseDto winCountRankResponseDto = WinCountRankResponseDto.builder()
                        .rank(i+1)
                        .nickName(oddWinList.get(i).getMember().getNickName())
                        .totalWinCount(oddWinList.get(i).getWinCount())
                        .build();
                oddWinListDto.add(winCountRankResponseDto);
            }
            // 벌어들인 포인트별
            List<OddEvenResult> oddPointList = oddEvenResultRepository.findByOrderByEarnPointDesc();
            List<EarnPointResponseDto> oddEarnPointListDto = new ArrayList<>();
            for(int i = 0, n = oddPointList.size(); i < n && i < 30; i++) {
                EarnPointResponseDto earnPointResponseDto = EarnPointResponseDto.builder()
                        .rank(i+1)
                        .nickName(oddPointList.get(i).getMember().getNickName())
                        .earnPoint(oddPointList.get(i).getEarnPoint())
                        .build();
                oddEarnPointListDto.add(earnPointResponseDto);
            }
            return ResponseDto.success(GameRanking.builder()
                    .gameTitle("홀짝게임")
                    .WinCountList(oddWinListDto)
                    .earnPointList(oddEarnPointListDto)
                    .build());
        }

        // 주사위 게임
        if(id == 2) {
            // 이긴 횟수별
            List<DiceResult> diceResultList = diceResultRepository.findByOrderByWinCountDesc();
            List<WinCountRankResponseDto> diceWinListDto = new ArrayList<>();
            for(int i = 0, n = diceResultList.size(); i < n && i < 30; i++) {
                WinCountRankResponseDto winCountRankResponseDto = WinCountRankResponseDto.builder()
                        .rank(i+1)
                        .nickName(diceResultList.get(i).getMember().getNickName())
                        .totalWinCount(diceResultList.get(i).getWinCount())
                        .build();
                diceWinListDto.add(winCountRankResponseDto);
            }
            // 벌어들인 포인트별
            List<DiceResult> dicePointList = diceResultRepository.findByOrderByEarnPointDesc();
            List<EarnPointResponseDto> diceEarnPointListDto = new ArrayList<>();
            for(int i = 0, n = dicePointList.size(); i < n && i < 30; i++) {
                EarnPointResponseDto earnPointResponseDto = EarnPointResponseDto.builder()
                        .rank(i+1)
                        .nickName(dicePointList.get(i).getMember().getNickName())
                        .earnPoint(dicePointList.get(i).getEarnPoint())
                        .build();
                diceEarnPointListDto.add(earnPointResponseDto);
            }
            return ResponseDto.success(GameRanking.builder()
                    .gameTitle("주사위게임")
                    .WinCountList(diceWinListDto)
                    .earnPointList(diceEarnPointListDto)
                    .build());
        }

        // 로또 게임
        if(id == 3) {
            // 벌어들인 포인트별
            List<LottoResult> lottoResultList = lottoResultRepository.findByOrderByEarnPointDesc();
            List<EarnPointResponseDto> lottoEarnPointListDto = new ArrayList<>();
            for(int i = 0, n = lottoResultList.size(); i < n && i < 30; i++) {
                EarnPointResponseDto earnPointResponseDto = EarnPointResponseDto.builder()
                        .rank(i+1)
                        .nickName(lottoResultList.get(i).getMember().getNickName())
                        .earnPoint(lottoResultList.get(i).getEarnPoint())
                        .build();
                lottoEarnPointListDto.add(earnPointResponseDto);
            }
            return ResponseDto.success(LottoRanking.builder()
                     .gameTitle("로또게임")
                     .earnPointList(lottoEarnPointListDto)
                     .build());
        }
        // 카운터 게임
        if(id == 4) {
            // 최대로 클릭한 횟수별
            List<CounterResult> counterResultList = counterResultRepository.findByOrderByMaxCountDesc();
            List<MaxCountResponseDto> counterMaxListDto = new ArrayList<>();
            for(int i = 0, n = counterResultList.size(); i < n && i < 30; i++) {
                MaxCountResponseDto maxCountResponseDto = MaxCountResponseDto.builder()
                        .rank(i+1)
                        .nickName(counterResultList.get(i).getMember().getNickName())
                        .maxCount(counterResultList.get(i).getMaxCount())
                        .build();
                counterMaxListDto.add(maxCountResponseDto);
            }
            return ResponseDto.success(CountRanking.builder()
                    .gameTitle("카운터게임")
                    .maxCountList(counterMaxListDto)
                    .build());
        }

        return ResponseDto.fail("RANKING_ERR","잘못된 게임 아이디입니다.");
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
