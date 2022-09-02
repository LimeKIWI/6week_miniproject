package com.example.week6project.controller;

import com.example.week6project.Dto.ResponseDto;
import com.example.week6project.Dto.requestDto.*;
import com.example.week6project.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.ParseException;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @RequestMapping(value = "/api/member/signup", method = RequestMethod.POST)
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }

    @RequestMapping(value = "/api/member/chkName", method = RequestMethod.POST)
    public ResponseDto<?> duplicateCheckNickname(@RequestBody @Valid NicknameDuplicateCheckRequestDto requestDto){
        return memberService.checkNickname(requestDto);
    }

    @RequestMapping(value = "/api/member/chkId", method = RequestMethod.POST)
    public ResponseDto<?> duplicateCheckId(@RequestBody @Valid IdDuplicateCheckRequestDto requestDto){
        return memberService.checkId(requestDto);
    }

    @RequestMapping(value = "/api/member/chkAdult", method = RequestMethod.POST)
    public ResponseDto<?> checkAdult(@RequestBody @Valid AdultCheckRequestDto requestDto) throws ParseException {
        return memberService.checkAdult(requestDto);
    }

    @RequestMapping(value = "/api/member/login", method = RequestMethod.POST)
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response){
        return memberService.login(requestDto, response);
    }
}
