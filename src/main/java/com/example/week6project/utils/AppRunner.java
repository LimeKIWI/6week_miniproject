package com.example.week6project.utils;

import com.example.week6project.domain.Member;
import com.example.week6project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        if(memberRepository.findByNickName("탈퇴한 사용자입니다.").isEmpty()) {
            Member member = Member.builder()
                    .id("8R95UIIyjxwrAkuBRiMd")
                    .nickName("탈퇴한 사용자입니다.")
                    .birthDate(20000101)
                    .point(0)
                    .passWord("UFGFyXgOgLQ53xeICQYp")
                    .totalWinCount(0)
                    .build();
            memberRepository.save(member);
        }
        System.out.println("실행시점");
    }
}
