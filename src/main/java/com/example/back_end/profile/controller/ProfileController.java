package com.example.back_end.profile.controller;

import com.example.back_end.profile.service.ProfileService;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final AuthenticationUserUtils userUtils;

    @GetMapping("/getProfile")
    public ResponseEntity<?> getUserProfile() {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result= profileService.getUserProfile(currentUserId);
        return ResponseEntity.ok(result);
    }
}
