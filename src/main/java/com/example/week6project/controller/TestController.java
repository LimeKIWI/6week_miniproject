package com.example.week6project.controller;

import com.example.week6project.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final TestService testService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String createPost(HttpServletRequest request) {
        return testService.test(request);
    }
}
