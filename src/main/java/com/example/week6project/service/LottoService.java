package com.example.week6project.service;

import com.example.week6project.controller.request.LottoRequestDto;
import com.example.week6project.controller.response.LottoResponseDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.Lotto;
import com.example.week6project.domain.Member;
import com.example.week6project.repository.LottoRepository;
import com.example.week6project.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LottoService {

    private final GameService gameService;
    private final MemberRepository memberRepository;
    private final LottoRepository lottoRepository;

    int lottoPoint = 1000; //로또 금액

    /*
    1등 : 6개 번호 모두 일치
        => 총 포인트 중 4~5등을 제외한 포인트의 75%
    2등 : 5개 번호 일치 + 보너스볼 번호 일치
        => 총 포인트 중 4~5등을 제외한 포인트의 12.5%
    3등 : 5개 번호 일치
        => 총 포인트 중 4~5등을 제외한 포인트의 12.5%
    4등 : 4개 번호 일치
        => 로또 금액 * 50
    5등 : 3개 번호 일치
        => 로또 금액 * 5
     */
    // 제출 번호 저장 및 당첨금 설정

    public ResponseDto<?> saveNum(LottoRequestDto lottoRequestDto, HttpServletRequest request){
        ResponseDto<?> chkResponse = gameService.validateCheck(request);
        if (!chkResponse.isSuccess())
            return chkResponse;

        Member memberData =  (Member) chkResponse.getData();
        Member member = memberRepository.findById(memberData.getId()).get();
        Lotto lotto = Lotto.builder()
                .member(member)
                .num1(lottoRequestDto.getNum1())
                .num2(lottoRequestDto.getNum2())
                .num3(lottoRequestDto.getNum3())
                .num4(lottoRequestDto.getNum4())
                .num5(lottoRequestDto.getNum5())
                .num6(lottoRequestDto.getNum6())
                .result(0)
                .build();

        lottoRepository.save(lotto);
        return ResponseDto.success("로또 구매완료!");
    }

    @Transactional
    public ResponseDto<?> runLotto(){
        int startId=findStartId();
        luckyNum();
        checkLotto(startId);
        LottoResponseDto temp_dto = lottoFinal(startId);
        LottoResponseDto lottoResponseDto = LottoResponseDto.builder()
                .num(luckyNum())
                .member1st(temp_dto.getMember1st())
                .member2rd(temp_dto.getMember2rd())
                .member3nd(temp_dto.getMember3nd())
                .money1st(temp_dto.getMoney1st())
                .build();
        return ResponseDto.success(lottoResponseDto);
    }
    // 번호 추첨

    public int[] luckyNum(){
        Random random = new Random();
        int[] luckynum=new int[7];
        luckynum[0]=random.nextInt(7)+1;
        for (int i = 1; i < 7; i++) {
            luckynum[i]=random.nextInt(7)+1;
            for (int j = 0; j < i; j++) {
                if(luckynum[i]==luckynum[j]){
                    i-=1;
                }
            }

        }
        return luckynum;
    }

    // 맞은 갯수 확인
    @Transactional
    public void checkLotto(int startId){
        // 번호 추첨
        int[] luckyNum = luckyNum();
        // 맞은 갯수 확인
        for (int i = startId; i < lottoRepository.count()+startId ; i++) {
            long l = i;
        Lotto lotto = lottoRepository.findById(l).get();
            for (int j = 0; j < 6; j++) {
                if(lotto.getNum1()==luckyNum[j]||lotto.getNum2()==luckyNum[j]||lotto.getNum3()==luckyNum[j]||lotto.getNum4()==luckyNum[j]||lotto.getNum5()==luckyNum[j]||lotto.getNum6()==luckyNum[j]){
                    System.out.println(lotto.getNum1());
                    lotto.plusOne();
                    System.out.println(lotto.getResult());
                }
            }
            if (lotto.getNum1()==luckyNum[6]||lotto.getNum2()==luckyNum[6]||lotto.getNum3()==luckyNum[6]||lotto.getNum4()==luckyNum[6]||lotto.getNum5()==luckyNum[6]||lotto.getNum6()==luckyNum[6]){
                lotto.plusBonus();
            }
        }
    }

    /*
    Lotto Entity에서 result에 따른 결과
    6= 1등
    15= 2등
    5= 3등
    4= 4등
    3= 5등
     */


    // 정산
    @Transactional
    public LottoResponseDto lottoFinal(int startId){
        if(startId==0){
            LottoResponseDto lottoResponseDto = LottoResponseDto.builder()
                    .money1st(0)
                    .member1st(0)
                    .member2rd(0)
                    .member3nd(0)
                    .build();
            return lottoResponseDto;
        }
        else {
            long countMember = lottoRepository.count();
            long totalPoint = 10000 + lottoPoint * countMember;// DB에서 남은 값도 받기

            List<Lotto> count1st = lottoRepository.findByResult(6);
            List<Lotto> count2nd = lottoRepository.findByResult(15);
            List<Lotto> count3rd = lottoRepository.findByResult(5);
            List<Lotto> count4th = lottoRepository.findByResult(4);
            List<Lotto> count5th = lottoRepository.findByResult(3);


            long point4th = lottoPoint * 10;
            long point5th = lottoPoint * 5;
            totalPoint = totalPoint - (point4th * count4th.size()) - (point5th * count5th.size());
            long point1st = (long) (totalPoint * 0.74);
            System.out.println("1 : "+count1st.size());
            System.out.println("2 : "+count2nd.size());
            System.out.println("3 : "+count3rd.size());
            if (count1st.size() != 0) {
                point1st = point1st / count1st.size();
            }
            long point2nd = (long) (totalPoint * 0.13);
            if (count2nd.size() != 0) {
                point2nd = point1st / count2nd.size();
            }
            long point3rd = (long) (totalPoint * 0.13);
            if (count3rd.size() != 0) {
                point3rd = point3rd / count1st.size();
            }
            for (int i = startId; i < count1st.size() + startId; i++) {
                int index=0;
                Member member = count1st.get(index).getMember();
                member.addPoint((int) point1st);
                totalPoint -= point1st;
                index+=1;
            }
            for (int i = startId; i < count2nd.size() + startId; i++) {
                int index=0;
                Member member = count2nd.get(index).getMember();
                member.addPoint((int) point2nd);
                totalPoint -= point2nd;
                index+=1;
            }
            for (int i = startId; i < count3rd.size() + startId; i++) {
                int index=0;
                Member member = count3rd.get(index).getMember();
                member.addPoint((int) point3rd);
                totalPoint -= point3rd;
                index+=1;
            }
            LottoResponseDto lottoResponseDto = LottoResponseDto.builder()
                    .money1st((int) point1st)
                    .member1st(count1st.size())
                    .member2rd(count2nd.size())
                    .member3nd(count3rd.size())
                    .build();
            System.out.println("By Final"+point1st);
            // +남은 totalPoint는 DB에 저장

            // 로또 구매 DB초기화
            lottoRepository.deleteAll();
            return lottoResponseDto;
        }
    }

    //LottoRepo에서 첫 Id(Id의 최솟값) 찾기 ->DB에 deleteall을 적용해도 Id는 1이아닌 삭제된 Id이후부터 시작
    public int findStartId(){
        long temp_id=0;
        boolean checkId=false;
        if(lottoRepository.count()!=0){
            while (checkId==false){
                if(!lottoRepository.existsById(temp_id)){
                    temp_id+=1;
                }
                else {
                    checkId=true;
                }
            }
        }
        int startId=(int) temp_id;
        return startId;
    }

}

