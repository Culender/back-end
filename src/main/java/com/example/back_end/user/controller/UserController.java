package com.example.back_end.user.controller;

import com.example.back_end.user.dto.SignInReqDto;
import com.example.back_end.user.dto.SignInResDto;
import com.example.back_end.user.dto.SignUpDto;
import com.example.back_end.user.service.UserService;
import com.example.back_end.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping(value = "/signUp", consumes = {"multipart/form-data"})
    public ResponseEntity<?> signUp(@Valid @ModelAttribute SignUpDto dto) {
        CustomApiResponse<?> result = userService.signUp(dto);
        return ResponseEntity.ok(result);
    }

    //로그인
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInReqDto dto) {
        CustomApiResponse<?> result = userService.signIn(dto);
        return ResponseEntity.ok(result);
    }
}
