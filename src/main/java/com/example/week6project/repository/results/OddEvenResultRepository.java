package com.example.week6project.repository.results;

import com.example.week6project.domain.Member;
import com.example.week6project.domain.results.OddEvenResult;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OddEvenResultRepository extends JpaRepository<OddEvenResult, Long> {
    OddEvenResult findByMember(Member member);
}
