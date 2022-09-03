package com.example.week6project.repository.results;

import com.example.week6project.domain.Member;
import com.example.week6project.domain.results.OddEvenResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OddEvenResultRepository extends JpaRepository<OddEvenResult, Long> {
    Optional<OddEvenResult> findByMember(Member member);
}
