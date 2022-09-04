package com.example.week6project.repository;

import com.example.week6project.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByNickName(String nickname);
    List<Member> findAllByOrderByTotalWinCountDesc();
    List<Member> findAllByOrderByPointDesc();
}
