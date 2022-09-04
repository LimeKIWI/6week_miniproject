package com.example.week6project.repository.results;

import com.example.week6project.domain.Member;
import com.example.week6project.domain.results.CounterResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CounterResultRepository extends JpaRepository<CounterResult, Long> {
    CounterResult findByMember(Member member);
    List<CounterResult> findByOrderByMaxCountDesc();
}
