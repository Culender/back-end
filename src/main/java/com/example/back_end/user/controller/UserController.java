package com.example.back_end.user.controller;

import com.example.back_end.user.dto.SignUpDto;
import com.example.back_end.user.service.UserService;
import com.example.back_end.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto dto) {
        String result = userService.signUp(dto);
        return ResponseEntity.ok(result);
    }
}
