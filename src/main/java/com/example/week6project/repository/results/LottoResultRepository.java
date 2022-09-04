package com.example.week6project.repository.results;

import com.example.week6project.domain.Member;
import com.example.week6project.domain.results.LottoResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LottoResultRepository extends JpaRepository<LottoResult, Long> {
    LottoResult findByMember(Member member);
    List<LottoResult> findByOrderByEarnPointDesc();
}
