package com.example.week6project.controller;

import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @RequestMapping(value = "/api/ranking", method = RequestMethod.GET)
    public ResponseDto<?> getTotalRanking(HttpServletRequest request) {
        return rankingService.getTotalRanking(request);
    }

    @RequestMapping(value = "/api/ranking/{id}", method = RequestMethod.GET)
    public ResponseDto<?> getGameRanking(@PathVariable Long id, HttpServletRequest request) {
        return rankingService.getGameRanking(id, request);
    }
}
