package com.example.week6project.service;


import com.example.week6project.domain.Member;
import com.example.week6project.domain.UserDetailsImpl;
import com.example.week6project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Member> member = memberRepository.findByNickName(username);
    return member
            .map(UserDetailsImpl::new)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
  }
}
