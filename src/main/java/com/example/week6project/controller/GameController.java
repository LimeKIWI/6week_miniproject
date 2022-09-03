package com.example.week6project.controller;

import com.example.week6project.controller.request.DiceRequestDto;
import com.example.week6project.controller.request.LottoRequestDto;
import com.example.week6project.controller.request.OddEvenRequestDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.service.GameService;
import com.example.week6project.service.LottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    private final LottoService lottoService;

    // 홀짝
    @RequestMapping (value = "/api/game/oddeven", method = RequestMethod.POST)
    public ResponseDto<?> runOddEven (@RequestBody OddEvenRequestDto oddEvenRequestDto, HttpServletRequest request) {
        return gameService.runOddEven(oddEvenRequestDto, request);
    }

    // 주사위
    @RequestMapping (value = "/api/game/dice", method = RequestMethod.POST)
    public ResponseDto<?> runDice (@RequestBody DiceRequestDto diceRequestDto, HttpServletRequest request) {
        return gameService.runDice(diceRequestDto, request);
    }

    // 로또구매
    @RequestMapping (value = "/api/game/lotto", method = RequestMethod.POST)
    public ResponseDto<?> runLotto (@RequestBody LottoRequestDto lottoRequestDto, HttpServletRequest request) {
        return lottoService.saveNum(lottoRequestDto,request);
    }

    // TEST 로또 결과
    @RequestMapping (value = "/api/game/lottotest", method = RequestMethod.GET)
    public ResponseDto<?> testLotto () {
        return lottoService.runLotto();
    }

    // 카운터
    @RequestMapping (value = "/api/game/counter", method = RequestMethod.POST)
    public ResponseDto<?> runCounter () {
        return ResponseDto.success("성공");
    }
}
