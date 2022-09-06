package com.example.week6project.repository;

import com.example.week6project.domain.Lotto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LottoRepository extends JpaRepository<Lotto, Long> {
    List<Lotto> findByLottoServerId(long lastid);

    List<Lotto> findAllByLottoServerIdAndResult(long lottoserver_id,long result);


    List<Lotto> findAllByMemberId(String myId);

    List<Lotto> findAllByLottoServerId(long lottosever_id);
}
