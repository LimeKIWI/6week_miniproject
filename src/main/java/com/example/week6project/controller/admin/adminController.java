package com.example.week6project.controller.admin;

import com.example.week6project.controller.request.PointRequestDto;
import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.service.AdminService;
import com.example.week6project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class adminController {

    private final AdminService adminService;

    // 어드민_포인트주기
    @RequestMapping(value = "/api/adminPage/{id}", method = RequestMethod.POST)
    public ResponseDto<?> addPointByAdmin(@PathVariable String id, @RequestBody PointRequestDto requestDto) {
        return adminService.addPointByAdmin(id, requestDto);
    }

    // 어드민_사용자조회
    @RequestMapping(value = "/api/adminPage", method = RequestMethod.GET)
    public ResponseDto<?> infoAllMember() {
        return adminService.infoAllMember();
    }


    /*

    //TEST- Admin생성
    @RequestMapping(value = "/api/member/admin", method = RequestMethod.POST)
    public com.example.week6project.dto.ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        return memberService.createAdmin(requestDto);
    }

     */
}
