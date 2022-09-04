package com.example.week6project.repository.results;

import com.example.week6project.domain.Member;
import com.example.week6project.domain.results.DiceResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DiceResultRepository extends JpaRepository<DiceResult, Long> {
    DiceResult findByMember(Member member);
    List<DiceResult> findByOrderByWinCountDesc();
    List<DiceResult> findByOrderByEarnPointDesc();
}
