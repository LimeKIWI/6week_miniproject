package com.example.week6project.repository;

import com.example.week6project.domain.Lotto;
import com.example.week6project.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LottoRepository extends JpaRepository<Lotto, Long> {
    List<Lotto> findByResult(long result);

    List<Lotto> findByLottoServerId(long lastid);
}
