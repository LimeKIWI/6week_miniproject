package com.example.week6project.service;

import com.example.week6project.controller.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class RankingService {
    public ResponseDto<?> getTotalRanking(HttpServletRequest request) {
        return null;
    }

    public ResponseDto<?> getGameRanking(Long id, HttpServletRequest request) {
        return null;
    }
}
