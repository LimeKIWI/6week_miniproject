package com.example.week6project.controller;

import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.dto.requestDto.NicknameDuplicateCheckRequestDto;
import com.example.week6project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 마이페이지
    @RequestMapping (value = "/api/user", method = RequestMethod.GET)
    public ResponseDto<?> getMyPage(HttpServletRequest request) {
        return userService.getMyPage(request);
    }

    // 프로필 이미지 업로드
    @RequestMapping (value = "/api/user/image", method = RequestMethod.PATCH)
    public ResponseDto<?> imageUpload(@RequestPart("image") MultipartFile multipartFile, HttpServletRequest request) {
        return userService.profileImageUpload(multipartFile, request);
    }

    // 회원정보수정
    @RequestMapping (value = "/api/user", method = RequestMethod.PATCH)
    public ResponseDto<?> updateInfo(@RequestBody NicknameDuplicateCheckRequestDto nicknameRequestDto, HttpServletRequest request, HttpServletResponse response) {
        return userService.updateInfo(nicknameRequestDto, request, response);
    }

    // 회원탈퇴 withDrawal
    @RequestMapping (value = "/api/user", method = RequestMethod.DELETE)
    public ResponseDto<?> withdrawals(HttpServletRequest request) {
        return userService.withDrawal(request);
    }
}
