package com.example.back_end.recordLike.controller;

import com.example.back_end.domain.RecordLike;
import com.example.back_end.recordLike.service.RecordLikeService;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recordLike")
@RequiredArgsConstructor
public class RecordLikeController {

    private final RecordLikeService recordLikeService;
    private final AuthenticationUserUtils userUtils;

    //좋아요
    @PostMapping("/like")
    public ResponseEntity<?> like (@RequestParam Long recordId){
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> response =recordLikeService.like(recordId,currentUserId);
        return ResponseEntity.ok(response);
    }

    //좋아요 취소
    @PostMapping("/unlike")
    public ResponseEntity<?> unlike (@RequestParam Long recordId){
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> response =recordLikeService.unlike(recordId,currentUserId);
        return ResponseEntity.ok(response);
    }
}
