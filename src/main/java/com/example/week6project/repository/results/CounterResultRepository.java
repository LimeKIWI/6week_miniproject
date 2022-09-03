package com.example.week6project.repository.results;

import com.example.week6project.domain.Member;
import com.example.week6project.domain.results.CounterResult;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CounterResultRepository extends JpaRepository<CounterResult, Long> {
    CounterResult findByMember(Member member);
}
