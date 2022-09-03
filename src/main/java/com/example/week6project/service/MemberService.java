package com.example.week6project.service;

import com.example.week6project.domain.results.DiceResult;
import com.example.week6project.domain.results.OddEvenResult;
import com.example.week6project.dto.TokenDto;
import com.example.week6project.dto.requestDto.*;
import com.example.week6project.dto.ResponseDto;
import com.example.week6project.repository.results.CounterResultRepository;
import com.example.week6project.repository.results.DiceResultRepository;
import com.example.week6project.repository.results.LottoResultRepository;
import com.example.week6project.repository.results.OddEvenResultRepository;
import com.example.week6project.security.TokenProvider;
import com.example.week6project.domain.Member;
import com.example.week6project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static com.example.week6project.domain.Authority.ROLE_MEMBER;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    private final OddEvenResultRepository oddEvenResultRepository;
    private final DiceResultRepository diceResultRepository;
    private final CounterResultRepository counterResultRepository;
    private final LottoResultRepository lottoResultRepository;

    //아이디 중복 검사
    @Transactional
    public ResponseDto<Object> checkId(IdDuplicateCheckRequestDto idRequestDto){
        //
        String temp_id=idRequestDto.getId();
        Member member = isPresentMemberById(temp_id);

        if(member!=null){
            return ResponseDto.fail("Duplicate ID","중복된 아이디 입니다.");
        }

        else {
            return ResponseDto.success("success");
        }
    }

    //닉네임 중복 검사
    @Transactional
    public ResponseDto<Object> checkNickname(NicknameDuplicateCheckRequestDto nicknameRequestDto){
        String temp_nickname=nicknameRequestDto.getNickName();
        Member member = isPresentMemberByNickname(temp_nickname);

        if(member!=null){
            return ResponseDto.fail("Duplicate Nickname","중복된 닉네임 입니다.");
        }

        else {
            return ResponseDto.success("success");
        }
    }
    // 성인인증
    public ResponseDto<?> checkAdult(AdultCheckRequestDto requestDto) throws ParseException {
        String temp_age=requestDto.getBirthDate();
        boolean adult = adultCertification(temp_age);
        if (adult==false){
            return ResponseDto.fail("No Adult","미성년자는 회원가입 할 수 없습니다.");
        }
        else {return ResponseDto.success("성인 인증이 완료되었습니다.");
        }
    }

    //결산테이블 생성
    private void createResult(Member member) {
        oddEvenResultRepository.save(OddEvenResult.builder()
                .member(member)
                .winCount(0)
                .earnPoint(0)
                .playCount(0)
                .build());

        diceResultRepository.save(DiceResult.builder()
                .member(member)
                .winCount(0)
                .earnPoint(0)
                .playCount(0)
                .build());
    }

    //회원가입
    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        Member member = Member.builder()
                .id(requestDto.getId())
                .nickname(requestDto.getNickname())
                .birthDate(Integer.parseInt(requestDto.getBirthDate()))
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .point(100)
                .userRole(ROLE_MEMBER)
                .build();
        memberRepository.save(member);

        // 유저 결산테이블 초기화
        createResult(member);

        return ResponseDto.success("회원가입 성공");
    }

    //로그인
    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response){
        Member member = isPresentMemberById(requestDto.getId());
        if(member==null){
            return ResponseDto.fail("Not Found Member","존재하지 않는 ID입니다.");
        }

        if(!validatePassword(requestDto.getId(), requestDto.getPassword())){
            return ResponseDto.fail("Wrong Password","잘못된 비밀번호 입니다.");
        }

        // 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);

        //헤더에 반환 to FE
        response.addHeader("Authorization","Bearer "+tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        return ResponseDto.success(member.getNickname());
    }

    // ID로 유저 조회(Only One)
    private Member isPresentMemberById(String id) {
        Optional<Member> member=memberRepository.findById(id);
        return member.orElse(null);
    }

    // 닉네임으로 유저 조회(Only One)
    private Member isPresentMemberByNickname(String nickname) {
        Optional<Member> member=memberRepository.findByNickname(nickname);
        return member.orElse(null);
    }

    private boolean validatePassword(String id,String pw){
        String MemberPw = isPresentMemberById(id).getPassword();
        return passwordEncoder.matches(pw,MemberPw);
    }

    private boolean adultCertification(String birthDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = format.parse(birthDate);
        Date now = new Date();
        int age = now.getYear()-date.getYear();
        if(date.getMonth()>now.getMonth()||date.getDate()>now.getDate())
            age-=1;
        if (age<19){
            return false;
        }
        else {
            return true;
        }
    }


}
