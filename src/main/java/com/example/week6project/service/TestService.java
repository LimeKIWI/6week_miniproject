package com.example.week6project.service;

import com.example.week6project.security.TokenProvider;
import com.example.week6project.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class TestService {
    private final TokenProvider tokenProvider;

    public String test(HttpServletRequest request){
        System.out.println("TestService"+request.getHeader("Authorization"));
        Member member = vaildateMember(request);
        return member.getId();
    }

    private Member vaildateMember(HttpServletRequest request) {
        System.out.println("TestService"+"vaildateMember");
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            System.out.println("null!!");
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

}
