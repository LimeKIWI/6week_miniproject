package com.example.week6project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class Scheduler {
    private final LottoService lottoService;


    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void autoLotto(){
        lottoService.runLotto();
    }
}
