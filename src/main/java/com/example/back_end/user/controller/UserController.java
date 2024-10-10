package com.example.back_end.user.controller;

import com.example.back_end.user.dto.SignInReqDto;
import com.example.back_end.user.dto.SignInResDto;
import com.example.back_end.user.dto.SignUpDto;
import com.example.back_end.user.dto.UpdateProfileDto;
import com.example.back_end.user.service.UserService;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.util.AuthenticationUserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationUserUtils userUtils;
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

    //프로필 수정
    @PatchMapping(value = "/updateProfile", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateProfile(@Valid @ModelAttribute UpdateProfileDto updateProfileDto) {
        String currentUser = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = userService.updateProfile(currentUser, updateProfileDto);
        return ResponseEntity.ok(result);
    }
}
