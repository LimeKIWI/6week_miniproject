package com.example.week6project.service;

import com.example.week6project.controller.request.LottoRequestDto;
import com.example.week6project.controller.response.LottoMyResultResponseDto;
import com.example.week6project.controller.response.LottoResponseDto;
import com.example.week6project.controller.response.LottoResultResponseDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.Lotto;
import com.example.week6project.domain.LottoServer;
import com.example.week6project.domain.Member;
import com.example.week6project.repository.LottoRepository;
import com.example.week6project.repository.LottoServerRepository;
import com.example.week6project.repository.MemberRepository;
import com.example.week6project.repository.results.LottoResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    private final LottoResultRepository lottoResultRepository;

    //상수

    static final int LOTTO_POINT=1000; // 로또 금액
    static final int LOTTO_MAX_NUM=20; // 로또 추첨 범위
    static final int BASIC_1ST_POINT = 15000000; // 1등 기본 당첨금
    static final int BASIC_2ND_POINT = 3000000; // 2등 기본 당첨금
    static final int BASIC_3RD_POINT = 200000; // 3등 기본 당첨금


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



    //매시 00분 실행
    @Transactional
    public ResponseDto<?> runLotto(){
        long lastId=lottoServerRepository.count(); //로또 회차 구하기
        int[] luckyNum=luckyNum();
        lottoServerRepository.findById(lastId).get().setLuckyNum(luckyNum);
        checkLotto(lastId,luckyNum);

        LottoResponseDto temp_dto = lottoFinal(lastId);
        LottoResponseDto lottoResponseDto = LottoResponseDto.builder()
                .num(luckyNum)
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
        // 구매 시간 확인
        if(!timeCheck()){
            return ResponseDto.success("57분~03분 사이는 정산시간으로 로또구매가 불가능합니다.");
        }

        // 구매 회차 확인
        long lastId=lottoServerRepository.count();
        LottoServer nowSever=lottoServerRepository.findById(lastId).get();

        // 구매자 확인
        ResponseDto<?> chkResponse = gameService.validateCheck(request);
        if (!chkResponse.isSuccess())
            return chkResponse;

        Member memberData =  (Member) chkResponse.getData();
        Member member = memberRepository.findById(memberData.getId()).get();

        // 구매 금액 지불가능여부 확인
        if (member.getPoint()<1000){
            return ResponseDto.success("로또를 구매하기 위한 포인트가 부족합니다");
        }

        //구매 정보 저장
        Lotto lotto;

        // 번호 자동 선택
        if(lottoRequestDto.getNum1()==0){
            int[] randomNum=luckyNum();
            lotto = Lotto.builder()
                    .member(member)
                    .lottoServer(nowSever)
                    .num1(randomNum[0])
                    .num2(randomNum[1])
                    .num3(randomNum[2])
                    .num4(randomNum[3])
                    .num5(randomNum[4])
                    .num6(randomNum[5])
                    .result(0)
                    .earnPoint(0)
                    .build();
        }
        // 번호 수동 선택
        else {
            lotto = Lotto.builder()
                    .member(member)
                    .lottoServer(nowSever)
                    .num1(lottoRequestDto.getNum1())
                    .num2(lottoRequestDto.getNum2())
                    .num3(lottoRequestDto.getNum3())
                    .num4(lottoRequestDto.getNum4())
                    .num5(lottoRequestDto.getNum5())
                    .num6(lottoRequestDto.getNum6())
                    .result(0)
                    .earnPoint(0)
                    .build();
        }

        member.addPoint(-LOTTO_POINT);
        int[] numList=new int[6];
        numList[0]= lotto.getNum1();
        numList[1]= lotto.getNum2();
        numList[2]= lotto.getNum3();
        numList[3]= lotto.getNum4();
        numList[4]= lotto.getNum5();
        numList[5]= lotto.getNum6();



        nowSever.plusPoint(LOTTO_POINT);

        lottoRepository.save(lotto);
        return ResponseDto.success(numList);
    }


    // 번호 추첨

    public int[] luckyNum(){
        Random random = new Random();
        int[] luckynum=new int[7];
        luckynum[0]=random.nextInt(LOTTO_MAX_NUM)+1;
        for (int i = 1; i < 7; i++) {
            luckynum[i]=random.nextInt(LOTTO_MAX_NUM)+1;
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
    public void checkLotto(long lastid,int[] luckyNum){
        List<Lotto> lottoList=lottoRepository.findByLottoServerId(lastid);
        for (int i = 0; i < lottoList.size(); i++) {
            Lotto lotto=lottoList.get(i);
            for (int j = 0; j < 6; j++) {
                if(lotto.getNum1()==luckyNum[j]||lotto.getNum2()==luckyNum[j]||lotto.getNum3()==luckyNum[j]||lotto.getNum4()==luckyNum[j]||lotto.getNum5()==luckyNum[j]||lotto.getNum6()==luckyNum[j]){
                    lotto.plusOne();
                }
            }
            if (lotto.getNum1()==luckyNum[6]||lotto.getNum2()==luckyNum[6]||lotto.getNum3()==luckyNum[6]||lotto.getNum4()==luckyNum[6]||lotto.getNum5()==luckyNum[6]||lotto.getNum6()==luckyNum[6]){
                lotto.plusBonus();
            }

            lotto.rank();

        }
    }

    /*
    Lotto Entity에서 result에 따른 결과
    6= 1등
    15= 2등
    5= 3등
    4.14= 4등
    3,13= 5등
    else =6등(꽝)
     */


    // 정산
    @Transactional
    public LottoResponseDto lottoFinal(long lastId){
        List<Lotto> lottoList=lottoRepository.findByLottoServerId(lastId);
        LottoServer lottoServer=lottoServerRepository.findById(lastId).get();
        // DB에서 구매 총액 가져오기
        long totalPoint=lottoServer.getPoint();
        LottoResponseDto lottoResponseDto;

        //구매자가 0명일때
        if(lottoList.size()==0){
            lottoResponseDto = LottoResponseDto.builder()
                    .money1st(0)
                    .member1st(0)
                    .member2rd(0)
                    .member3nd(0)
                    .build();        }
        else {
            // 등수 확인
            List<Lotto> count1st = lottoRepository.findAllByLottoServerIdAndResult(lastId,1);
            List<Lotto> count2nd = lottoRepository.findAllByLottoServerIdAndResult(lastId,2);
            List<Lotto> count3rd = lottoRepository.findAllByLottoServerIdAndResult(lastId,3);
            List<Lotto> count4th = lottoRepository.findAllByLottoServerIdAndResult(lastId,4);
            List<Lotto> count5th = lottoRepository.findAllByLottoServerIdAndResult(lastId,5);
            List<Lotto> count6th = lottoRepository.findAllByLottoServerIdAndResult(lastId,6);



            // 구매금액으로부터 당첨 금액 분할
            long temp_point1st = (long) (totalPoint * 0.75);//  +15,000,000
            long temp_point2nd = (long) (totalPoint * 0.125);// +3,000,000
            long temp_point3rd = (long) (totalPoint * 0.125);// +200,000
            long temp_point4th = LOTTO_POINT * 20;
            long temp_point5th = LOTTO_POINT * 5;

            // 분할된 금액과 당첨금액 합산(최종 지급 금액)
            long point1st=temp_point1st+BASIC_1ST_POINT;
            long point2nd=temp_point2nd+BASIC_2ND_POINT;
            long point3rd=temp_point3rd+BASIC_3RD_POINT;
            long point4th=temp_point4th;
            long point5th=temp_point5th;


            // 당첨금액 분할시 0으로 나누어지는 오류 사전 방지
            if (count1st.size() != 0) {
                point1st = point1st / count1st.size();
            }

            if (count2nd.size() != 0) {
                point2nd = point2nd / count2nd.size();
            }

            if (count3rd.size() != 0) {
                point3rd = point3rd / count3rd.size();
            }

            // 당첨금액 DB에 저장
            lottoServer.setPoint(point1st,point2nd,point3rd);

            // 1등부터 당첨금 정산, 구매 총액에서 정산후 남은 금액은 다음 회차로 이월
            for (int i = 0; i < count1st.size(); i++) {
                Member member = count1st.get(i).getMember();
                member.addPoint((int) point1st);
                lottoResultRepository.findByMember(member).result(1,(int) point1st);
                count1st.get(i).setEarnPoint((int) point1st);
                lottoServer.plusPoint((int)-temp_point1st/count1st.size());
                totalPoint -= temp_point1st/count1st.size();
            }

            for (int i = 0; i < count2nd.size(); i++) {
                Member member = count2nd.get(i).getMember();
                member.addPoint((int) point2nd);
                lottoResultRepository.findByMember(member).result(2,(int) point2nd);
                count2nd.get(i).setEarnPoint((int) point2nd);
                lottoServer.plusPoint((int)-temp_point2nd/count2nd.size());
                totalPoint -= temp_point2nd/count2nd.size();
            }

            for (int i = 0; i < count3rd.size(); i++) {
                Member member = count3rd.get(i).getMember();
                member.addPoint((int) point3rd);
                lottoResultRepository.findByMember(member).result(3,(int) point3rd);
                count3rd.get(i).setEarnPoint((int) point3rd);
                lottoServer.plusPoint((int)-temp_point3rd/count3rd.size());
                totalPoint -= temp_point3rd/count3rd.size();
            }

            for (int i = 0; i < count4th.size(); i++) {
                Member member = count4th.get(i).getMember();
                member.addPoint((int)point4th);
                lottoResultRepository.findByMember(member).result(4,(int)point4th);
                count4th.get(i).setEarnPoint((int) point4th);
            }

            for (int i = 0; i < count5th.size(); i++) {
                Member member = count5th.get(i).getMember();
                member.addPoint((int)point5th);
                lottoResultRepository.findByMember(member).result(5,(int)point5th);
                count5th.get(i).setEarnPoint((int) point5th);
            }

            for (int i = 0; i < count6th.size(); i++) {
                Member member = count6th.get(i).getMember();
                lottoResultRepository.findByMember(member).result(6,0);
            }
            lottoResponseDto = LottoResponseDto.builder()
                    .money1st((int) point1st)
                    .member1st(count1st.size())
                    .member2rd(count2nd.size())
                    .member3nd(count3rd.size())
                    .build();

        }
        // +남은 totalPoint는 DB에 저장
        LottoServer nextLottoServer= LottoServer.builder()
                .point((int) totalPoint)
                .luckyNum1(0)
                .luckyNum2(0)
                .luckyNum3(0)
                .luckyNum4(0)
                .luckyNum4(0)
                .luckyNum5(0)
                .luckyNum6(0)
                .point1st(0)
                .point2nd(0)
                .point3rd(0)
                .build();

        lottoServerRepository.save(nextLottoServer);
        return lottoResponseDto;
    }

    //로또 최근 3회차 결과 확인(전체)
    public ResponseDto<?> lottoResult(){
        long lastIndex=lottoServerRepository.count(); //회차 확인
        int countIndex=0;
        List<LottoResultResponseDto> responseDtos = new ArrayList<>();
        if (lastIndex==1){
            return ResponseDto.fail("No Result","아직 로또가 진행되지 않았습니다. 로또추첨은 매시 정각에 진행되며 57분부터 03분까지는 구매가 불가능합니다.");
        }
        while (lastIndex>1&&countIndex<3){
            LottoServer lottoResult=lottoServerRepository.findById(lastIndex-1).get();

            int[] lucknumList=new int[6];
            lucknumList[0]=lottoResult.getLuckyNum1();
            lucknumList[1]=lottoResult.getLuckyNum2();
            lucknumList[2]=lottoResult.getLuckyNum3();
            lucknumList[3]=lottoResult.getLuckyNum4();
            lucknumList[4]=lottoResult.getLuckyNum5();
            lucknumList[5]=lottoResult.getLuckyNum6();

            List<String> member1stList=new ArrayList<>();
            List<Lotto> temp_1stList= lottoRepository.findAllByLottoServerIdAndResult(lastIndex-1,1L);
            for (int i = 0; i < temp_1stList.size(); i++) {
                String nickname1st=temp_1stList.get(i).getMember().getNickName();
                if(!member1stList.contains(nickname1st))
                    member1stList.add(temp_1stList.get(i).getMember().getNickName());
            }
            List<String> member2ndList=new ArrayList<>();
            List<Lotto> temp_2ndList= lottoRepository.findAllByLottoServerIdAndResult(lastIndex-1,2L);
            for (int i = 0; i < temp_2ndList.size(); i++) {
                String nickname2nd=temp_2ndList.get(i).getMember().getNickName();
                if(!member1stList.contains(nickname2nd))
                    member2ndList.add(temp_2ndList.get(i).getMember().getNickName());
            }
            List<String> member3rdList=new ArrayList<>();
            List<Lotto> temp_3rdList= lottoRepository.findAllByLottoServerIdAndResult(lastIndex,3L);
            for (int i = 0; i < temp_3rdList.size(); i++) {
                String nickname3rd=temp_3rdList.get(i).getMember().getNickName();
                if(!member1stList.contains(nickname3rd))
                    member3rdList.add(temp_3rdList.get(i).getMember().getNickName());
            }
            responseDtos.add(LottoResultResponseDto.builder()
                    .no((int) lastIndex-1)
                    .luckyNum(lucknumList)
                    .bonusNum(lottoResult.getBonusNum())
                    .point1st(lottoResult.getPoint1st())
                    .member1st(member1stList)
                    .point2nd(lottoResult.getPoint2nd())
                    .member2nd(member2ndList)
                    .point3nd(lottoResult.getPoint3rd())
                    .member3rd(member3rdList)
                    .build());
            lastIndex-=1;countIndex+=1;
        }
        return ResponseDto.success(responseDtos);

    }

    //로또 전체 개인 결과 확인(개인)
    public ResponseDto<?> myLottoAllResult(HttpServletRequest request){
        ResponseDto<?> chkResponse = gameService.validateCheck(request);
        if (!chkResponse.isSuccess())
            return chkResponse;

        Member memberData =  (Member) chkResponse.getData();
        String myId=memberData.getId();
        List<Lotto> myLottoList=lottoRepository.findAllByMemberId(myId);
        List<LottoMyResultResponseDto> responseDtoList = new ArrayList<>();


        for (int i = 0; i < myLottoList.size(); i++) {
            long no=myLottoList.get(i).getLottoServer().getId();
            LottoServer temp_server=lottoServerRepository.findById(no).get();
            // 추첨 완료된 Lotto
            if(temp_server.getLuckyNum1()!=0) {
                int[] myNumList = new int[6];
                myNumList[0] = myLottoList.get(i).getNum1();
                myNumList[1] = myLottoList.get(i).getNum2();
                myNumList[2] = myLottoList.get(i).getNum3();
                myNumList[3] = myLottoList.get(i).getNum4();
                myNumList[4] = myLottoList.get(i).getNum5();
                myNumList[5] = myLottoList.get(i).getNum6();

                int[] luckyNumList = new int[6];
                luckyNumList[0] = temp_server.getLuckyNum1();
                luckyNumList[1] = temp_server.getLuckyNum2();
                luckyNumList[2] = temp_server.getLuckyNum3();
                luckyNumList[3] = temp_server.getLuckyNum4();
                luckyNumList[4] = temp_server.getLuckyNum5();
                luckyNumList[5] = temp_server.getLuckyNum6();

                LottoMyResultResponseDto temp_dto = LottoMyResultResponseDto.builder()
                        .no((int) no)
                        .luckyNum(luckyNumList)
                        .bonusNum(temp_server.getBonusNum())
                        .MyNum(myNumList)
                        .rank((int) myLottoList.get(i).getResult())
                        .earnPoint(myLottoList.get(i).getEarnPoint())
                        .build();
                responseDtoList.add(temp_dto);
            }
            // 추첨이 진행되지않은 Lotto
            else {
                int[] myNumList = new int[6];
                myNumList[0] = myLottoList.get(i).getNum1();
                myNumList[1] = myLottoList.get(i).getNum2();
                myNumList[2] = myLottoList.get(i).getNum3();
                myNumList[3] = myLottoList.get(i).getNum4();
                myNumList[4] = myLottoList.get(i).getNum5();
                myNumList[5] = myLottoList.get(i).getNum6();
                LottoMyResultResponseDto temp_dto = LottoMyResultResponseDto.builder()
                        .no((int) no)
                        .luckyNum("아직 실행되지않은 회차입니다.")
                        .bonusNum(temp_server.getBonusNum())
                        .MyNum(myNumList)
                        .rank("아직 실행되지않은 회차입니다.")
                        .build();
                responseDtoList.add(temp_dto);
            }

        }
        return ResponseDto.success(responseDtoList);
    }

    //로또 개인 결과 확인(개인)
    public ResponseDto<?> myLottoResult(HttpServletRequest request){
        // 요청보낸 유저 확인
        ResponseDto<?> chkResponse = gameService.validateCheck(request);
        if (!chkResponse.isSuccess())
            return chkResponse;

        // 멤버 객체 받기
        Member memberData =  (Member) chkResponse.getData();
        String myId=memberData.getId();
        List<Lotto> temp_myLottoList=lottoRepository.findAllByMemberId(myId);
        if(temp_myLottoList.size()==0){
            return ResponseDto.success("구매한 로또가 없습니다.");
        }
        // 멤버가 구매한 로또중 가장 최근 회차 찾기
        long index=temp_myLottoList.get(0).getLottoServer().getId();
        for (int i = 0; i < temp_myLottoList.size(); i++) {
            if(index<temp_myLottoList.get(i).getLottoServer().getId()){
                index=temp_myLottoList.get(i).getLottoServer().getId();
            }
        }
        List<Lotto> myLottoList=lottoRepository.findAllByLottoServerId(index);
        List<LottoMyResultResponseDto> responseDtoList = new ArrayList<>();


        for (int i = 0; i < myLottoList.size(); i++) {
            long no=myLottoList.get(i).getLottoServer().getId();
            LottoServer temp_server=lottoServerRepository.findById(no).get();
            // 추첨 완료된 Lotto
            if(temp_server.getLuckyNum1()!=0) {
                int[] myNumList = new int[6];
                myNumList[0] = myLottoList.get(i).getNum1();
                myNumList[1] = myLottoList.get(i).getNum2();
                myNumList[2] = myLottoList.get(i).getNum3();
                myNumList[3] = myLottoList.get(i).getNum4();
                myNumList[4] = myLottoList.get(i).getNum5();
                myNumList[5] = myLottoList.get(i).getNum6();

                int[] luckyNumList = new int[6];
                luckyNumList[0] = temp_server.getLuckyNum1();
                luckyNumList[1] = temp_server.getLuckyNum2();
                luckyNumList[2] = temp_server.getLuckyNum3();
                luckyNumList[3] = temp_server.getLuckyNum4();
                luckyNumList[4] = temp_server.getLuckyNum5();
                luckyNumList[5] = temp_server.getLuckyNum6();

                LottoMyResultResponseDto temp_dto = LottoMyResultResponseDto.builder()
                        .no((int) no)
                        .luckyNum(luckyNumList)
                        .bonusNum(temp_server.getBonusNum())
                        .MyNum(myNumList)
                        .rank((int) myLottoList.get(i).getResult())
                        .earnPoint(myLottoList.get(i).getEarnPoint())
                        .build();
                responseDtoList.add(temp_dto);
            }
            // 추첨이 진행되지않은 Lotto
            else {
                int[] myNumList = new int[6];
                myNumList[0] = myLottoList.get(i).getNum1();
                myNumList[1] = myLottoList.get(i).getNum2();
                myNumList[2] = myLottoList.get(i).getNum3();
                myNumList[3] = myLottoList.get(i).getNum4();
                myNumList[4] = myLottoList.get(i).getNum5();
                myNumList[5] = myLottoList.get(i).getNum6();
                LottoMyResultResponseDto temp_dto = LottoMyResultResponseDto.builder()
                        .no((int) no)
                        .luckyNum("아직 실행되지않은 회차입니다.")
                        .bonusNum(temp_server.getBonusNum())
                        .MyNum(myNumList)
                        .rank("아직 실행되지않은 회차입니다.")
                        .build();
                responseDtoList.add(temp_dto);
            }

        }
        return ResponseDto.success(responseDtoList);
    }


    public boolean timeCheck(){
        Date date = new Date();
        int min=date.getMinutes();
        if (min < 3||min>57){
            return false;
        }
        else {
            return true;
        }
    }



}
