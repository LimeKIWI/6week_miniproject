package com.example.week6project.repository.results;

import com.example.week6project.domain.Member;
import com.example.week6project.domain.results.DiceResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiceResultRepository extends JpaRepository<DiceResult, Long> {
    Optional<DiceResult> findByMember(Member member);
}
