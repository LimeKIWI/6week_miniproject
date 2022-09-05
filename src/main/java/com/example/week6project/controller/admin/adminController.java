package com.example.week6project.controller.admin;

import com.example.week6project.controller.request.LottoRequestDto;
import com.example.week6project.controller.request.PointRequestDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.dto.requestDto.MemberRequestDto;
import com.example.week6project.service.AdminService;
import com.example.week6project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class adminController {

    private final AdminService adminService;
    private final MemberService memberService;

    @RequestMapping(value = "/adminPage/{id}", method = RequestMethod.POST)
    public ResponseDto<?> addPointByAdmin(@PathVariable String id, @RequestBody PointRequestDto requestDto) {
        return adminService.addPointByAdmin(id, requestDto);
    }

    @RequestMapping(value = "/adminPage", method = RequestMethod.GET)
    public ResponseDto<?> infoAllMember() {
        return adminService.infoAllMember();
    }

    //TEST- Admin생성
    @RequestMapping(value = "/api/member/admin", method = RequestMethod.POST)
    public com.example.week6project.dto.ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        return memberService.createAdmin(requestDto);
    }
}
