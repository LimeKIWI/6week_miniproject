package com.example.week6project.Service;

import com.example.week6project.Dto.TokenDto;
import com.example.week6project.Dto.requestDto.DuplicateCheckRequestDto;
import com.example.week6project.Dto.requestDto.LoginRequestDto;
import com.example.week6project.Dto.requestDto.MemberRequestDto;
import com.example.week6project.Dto.ResponseDto;
import com.example.week6project.Security.TokenProvider;
import com.example.week6project.domain.Member;
import com.example.week6project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.example.week6project.domain.Authority.ROLE_MEMBER;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenProvider tokenProvider;

    //아이디 중복 검사
    @Transactional
    public ResponseDto<Object> checkId(DuplicateCheckRequestDto idRequestDto){
        //
        String temp_id=idRequestDto.getIdOrNickname();
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
    public ResponseDto<Object> checkNickname(DuplicateCheckRequestDto nicknameRequestDto){
        System.out.println(nicknameRequestDto.toString());
        String temp_nickname=nicknameRequestDto.getIdOrNickname();
        System.out.println(temp_nickname);
        Member member = isPresentMemberByNickname(temp_nickname);

        if(member!=null){
            return ResponseDto.fail("Duplicate Nickname","중복된 닉네임 입니다.");
        }

        else {
            return ResponseDto.success("success");
        }
    }
    //회원가입
    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto){
        Member member = Member.builder()
                .id(requestDto.getId())
                .pw(passwordEncoder.encode(requestDto.getPw()))
                .nickname(requestDto.getNickname())
                .birthDate(Integer.parseInt(requestDto.getBirthDate()))
                .point(100)
                .userRole(ROLE_MEMBER)
                .build();
        memberRepository.save(member);
        return ResponseDto.success("회원가입 성공");
    }

    //로그인
    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response){
        Member member = isPresentMemberById(requestDto.getId());
        if(member==null){
            return ResponseDto.fail("Not Found Member","존재하지 않는 ID입니다.");
        }

        if(!validatePassword(requestDto.getId(), requestDto.getPw())){
            return ResponseDto.fail("Wrong Password","잘못된 비밀번호 입니다.");
        }

        // 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);

        //헤더에 반환 to FE
        response.addHeader("Access-Token","Bearer "+tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
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
        String MemberPw = isPresentMemberById(id).getPw();
        return passwordEncoder.matches(pw,MemberPw);
    }
}
