package com.example.week6project.controller;

import com.example.week6project.controller.request.CounterRequestDto;
import com.example.week6project.controller.request.DiceRequestDto;
import com.example.week6project.controller.request.LottoRequestDto;
import com.example.week6project.controller.request.OddEvenRequestDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.service.GameService;
import com.example.week6project.service.LottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    private final LottoService lottoService;

    // 홀짝
    @RequestMapping (value = "/api/game/oddeven", method = RequestMethod.POST)
    public ResponseDto<?> runOddEven(@RequestBody OddEvenRequestDto oddEvenRequestDto, HttpServletRequest request) {
        return gameService.runOddEven(oddEvenRequestDto, request);
    }

    // 주사위
    @RequestMapping (value = "/api/game/dice", method = RequestMethod.POST)
    public ResponseDto<?> runDice(@RequestBody DiceRequestDto diceRequestDto, HttpServletRequest request) {
        return gameService.runDice(diceRequestDto, request);
    }

    // 로또구매
    @RequestMapping (value = "/api/game/lotto", method = RequestMethod.POST)
    public ResponseDto<?> runLotto(@RequestBody LottoRequestDto lottoRequestDto, HttpServletRequest request) {
        return lottoService.saveNum(lottoRequestDto, request);
    }


    // TEST 로또 강제실행
    @RequestMapping (value = "/api/game/lottotest", method = RequestMethod.GET)
    public ResponseDto<?> testLotto() {
        return lottoService.runLotto();
    }



    // 로또 최근 3회차 결과 확인(전체)
    @RequestMapping (value = "/api/game/lottoresult", method = RequestMethod.GET)
    public ResponseDto<?> recentLottoResult() {
        return lottoService.lottoResult();
    }

    // 로또 개인 결과 확인
    @RequestMapping (value = "/api/game/lottomyresult", method = RequestMethod.GET)
    public ResponseDto<?> recentLottoResult(HttpServletRequest request) {
        return lottoService.myLottoResult(request);
    }

    // 카운터
    @RequestMapping (value = "/api/game/counter", method = RequestMethod.POST)
    public ResponseDto<?> runCounter(@RequestBody CounterRequestDto counterRequestDto, HttpServletRequest request) {
        return gameService.runCounter(counterRequestDto, request);
    }
}
