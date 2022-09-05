package com.example.week6project.service;

import com.example.week6project.controller.request.PointRequestDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.domain.Member;
import com.example.week6project.repository.MemberRepository;
import com.example.week6project.security.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    // 불러오기
    public ResponseDto<?> infoAllMember(){
        List<Member> tempList=memberRepository.findAll();
        List<infoDto> infoAllMember=new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            infoDto temp_dto= infoDto.builder()
                    .id(tempList.get(i).getId())
                    .point(tempList.get(i).getPoint())
                    .build();
            infoAllMember.add(temp_dto);
        }
        return ResponseDto.success(infoAllMember);
    }


    // 포인트 지급
    @Transactional
    public ResponseDto<?> addPointByAdmin(String id, PointRequestDto requestDto){
        int point = requestDto.getPoint();
        Member member = memberRepository.findById(id).get();
        // 만약 없는 아이디일시 NoSuchElementException 대신 커스텀Exception 출력
        member.addPoint(point);
        String dto = member.getNickName()+" " + point + "포인트 지급 완료";
        return ResponseDto.success(dto);

    }



    @Builder
    @Getter
    private static class infoDto{
        private String id;
        private int point;
    }








    /* ADMIN 체크 TEST

    public String testadmin(HttpServletRequest request){
        tokenProvider.getAuthentication(request).getAuthorities();
        return tokenProvider.getAuthentication(request).toString();

    }

    public ResponseDto<?> validateCheck(HttpServletRequest request) {
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

     */




}
