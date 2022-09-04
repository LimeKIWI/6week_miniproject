package com.example.week6project.service;

import com.example.week6project.controller.request.LottoRequestDto;
import com.example.week6project.controller.response.LottoResponseDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.Lotto;
import com.example.week6project.domain.LottoServer;
import com.example.week6project.domain.Member;
import com.example.week6project.repository.LottoRepository;
import com.example.week6project.repository.LottoServerRepository;
import com.example.week6project.repository.MemberRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LottoService {

    /*
    매시 00분 n+1회차 LottoServer 테이블 생성
            n회차 결산
            n회차 결산 후 남은금액 n+1회차로 넘기기
            ->서버 오픈시 1회차 자동생성

    매시 03분 ~ 57분 n+1회차 로또 구매 받기
     */

    private final GameService gameService;
    private final MemberRepository memberRepository;
    private final LottoRepository lottoRepository;
    private final LottoServerRepository lottoServerRepository;

    int lottoPoint = 1000; //로또 금액

    int lottoMaxNum=10;
    /* 왜 Java는 21억까지밖에 계산이 안되는것인가 ㅡㅡ
    MaxNum에 따른 확률 -> 기댓값을 통한 대략적인 당첨금액(단, 로또 금액은 1000p)
    1등 : 1 / 38760  -> 30,000,000
    2등 : 1 / 6460   ->  6,000,000
    3등 : 1 / 497    ->    400,000
    4등 : 1 / 33     ->     20,000
    5등 : 1 / 7      ->      5,000
     */

    /*
    <실제 로또 등수 및 당첨금 산정 방법>
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

    /*
    결론 :
    1등 15,000,000 + 총 포인트의 75%
    2등 3,000,000 + 총 포인트의 12.5%
    3등 200,000 + 총 포인트의 12.5%
    4등 20,000
    5등 5,000
    */


    @Transactional
    public void createLottoServer() {
        LottoServer lottoServer = new LottoServer(1L, 1000);
        lottoServerRepository.save(lottoServer);
    }

    //매시 00분 실행
    @Transactional
    public ResponseDto<?> runLotto(){
        long lastId=lottoServerRepository.count(); //로또 회차 구하기
        if(lastId == 0)
            createLottoServer();
        int startId=findStartId(); // 로또 구매자 테이블에서 시작하는 ID찾기
        luckyNum();
        checkLotto(startId);
        LottoResponseDto temp_dto = lottoFinal(startId,lastId);
        LottoResponseDto lottoResponseDto = LottoResponseDto.builder()
                .num(luckyNum())
                .member1st(temp_dto.getMember1st())
                .member2rd(temp_dto.getMember2rd())
                .member3nd(temp_dto.getMember3nd())
                .money1st(temp_dto.getMoney1st())
                .build();
        return ResponseDto.success(lottoResponseDto);
    }


    @Transactional
    // 제출 번호 저장 및 당첨금 설정
    public ResponseDto<?> saveNum(LottoRequestDto lottoRequestDto, HttpServletRequest request){
        ResponseDto<?> chkResponse = gameService.validateCheck(request);
        if (!chkResponse.isSuccess())
            return chkResponse;

        Member memberData =  (Member) chkResponse.getData();
        Member member = memberRepository.findById(memberData.getId()).get();


        if (member.getPoint()<1000){
            return ResponseDto.fail("Not Enough Point","로또를 구매하기 위한 포인트가 부족합니다");
        }
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

        member.addPoint(-lottoPoint);
        int[] numList=new int[6];
        numList[0]= lotto.getNum1();
        numList[1]= lotto.getNum2();
        numList[2]= lotto.getNum3();
        numList[3]= lotto.getNum4();
        numList[4]= lotto.getNum5();
        numList[5]= lotto.getNum6();



        long lastId=lottoServerRepository.count();
        LottoServer lottoServer = lottoServerRepository.findById(lastId).get();
        lottoServer.plusPoint(lottoPoint);

        lottoRepository.save(lotto);
        return ResponseDto.success(numList);
    }


    // 번호 추첨

    public int[] luckyNum(){
        Random random = new Random();
        int[] luckynum=new int[7];
        luckynum[0]=random.nextInt(lottoMaxNum)+1;
        for (int i = 1; i < 7; i++) {
            luckynum[i]=random.nextInt(lottoMaxNum)+1;
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
    public LottoResponseDto lottoFinal(int startId,long lastId){
        //구매자가 0명일때
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
            // DB에서 구매 총액 가져오기
            LottoServer lottoServer=lottoServerRepository.findById(lastId).get();
            long totalPoint=lottoServer.getPoint();

            // 등수 확인
            List<Lotto> count1st = lottoRepository.findByResult(6);
            List<Lotto> count2nd = lottoRepository.findByResult(15);
            List<Lotto> count3rd = lottoRepository.findByResult(5);
            List<Lotto> count4th = lottoRepository.findByResult(4);
            List<Lotto> count5th = lottoRepository.findByResult(3);



            // 당첨 금액 설정
            long point1st = (long) (totalPoint * 0.75);//  +15,000,000
            long point2nd = (long) (totalPoint * 0.125);// +3,000,000
            long point3rd = (long) (totalPoint * 0.125);// +200,000
            long point4th = lottoPoint * 20;
            long point5th = lottoPoint * 5;

            int index=0;

            // 당첨금액 분할시 0으로 나누어지는 오류 사전 방지
            if (count1st.size() != 0) {
                point1st = point1st / count1st.size();
            }

            if (count2nd.size() != 0) {
                point2nd = point1st / count2nd.size();
            }

            if (count3rd.size() != 0) {
                point3rd = point3rd / count3rd.size();
            }

            // 1등부터 당첨금 정산, 구매 총액에서 정산후 남은 금액은 다음 회차로 이월
            index=0;
            for (int i = startId; i < count1st.size() + startId; i++) {

                Member member = count1st.get(index).getMember();
                member.addPoint((int) point1st);
                member.addPoint((int) 15000000/count1st.size());
                lottoServer.plusPoint((int)-point1st);
                totalPoint -= point1st;
                index+=1;
            }

            index=0;
            for (int i = startId; i < count2nd.size() + startId; i++) {
                Member member = count2nd.get(index).getMember();
                member.addPoint((int) point2nd);
                member.addPoint((int) 3000000/count2nd.size());
                lottoServer.plusPoint((int)-point2nd);
                totalPoint -= point2nd;
                index+=1;
            }

            index=0;
            for (int i = startId; i < count3rd.size() + startId; i++) {
                Member member = count3rd.get(index).getMember();
                member.addPoint((int) point3rd);
                member.addPoint((int) 200000/count3rd.size());
                lottoServer.plusPoint((int)-point3rd);
                totalPoint -= point3rd;
                index+=1;
            }

            index=0;
            for (int i = startId; i < count4th.size() + startId; i++) {
                Member member = count4th.get(index).getMember();
                member.addPoint((int)point4th);
                index+=1;
            }

            index=0;
            for (int i = startId; i < count5th.size() + startId; i++) {
                Member member = count5th.get(index).getMember();
                member.addPoint((int)point5th);
                index+=1;
            }
            int firstMan=1;
            if(count1st.size()!=0){
                firstMan=count1st.size();
            }
            LottoResponseDto lottoResponseDto = LottoResponseDto.builder()
                    .money1st((int) point1st+15000000/firstMan)
                    .member1st(count1st.size())
                    .member2rd(count2nd.size())
                    .member3nd(count3rd.size())
                    .build();

            // +남은 totalPoint는 DB에 저장
            LottoServer nextLottoServer= LottoServer.builder()
                    .point((int) totalPoint)
                    .build();

            lottoServerRepository.save(nextLottoServer);

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

    public boolean timeCheck(){
        Date date = new Date();
        int min=date.getMinutes();
        System.out.println(date);
        if (min < 3||min>57){
            return false;
        }
        else {
            return true;
        }
    }



}

