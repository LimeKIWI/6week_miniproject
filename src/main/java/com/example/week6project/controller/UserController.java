package com.example.week6project.controller;

import com.example.week6project.controller.response.ResponseDto;
import com.example.week6project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping (value = "/api/user", method = RequestMethod.GET)
    public ResponseDto<?> getMyPage(HttpServletRequest request) {
        return userService.getMyPage(request);
    }

    @RequestMapping (value = "/api/user/image/{id}", method = RequestMethod.POST)
    public ResponseDto<?> imageUpload(@PathVariable Long id, @RequestPart("image") MultipartFile multipartFile, HttpServletRequest request) {
        return userService.profileImageUpload(id, multipartFile, request);
    }

}
