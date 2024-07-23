package org.example.fourtreesproject.user.controller;

import org.example.fourtreesproject.user.model.request.LoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// swagger에 로그인 페이지 추가용 controller
@RestController
public class LoginController {

    @PostMapping("/user/login")
    public void login(@RequestBody LoginRequest loginRequest) {

    }
}
