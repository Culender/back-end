package com.example.back_end.post.controller;

import com.example.back_end.post.service.PostLikeService;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/postLike")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final AuthenticationUserUtils userUtils;

    //좋아요
    @PostMapping("/like")
    public ResponseEntity<?> like (@RequestParam Long postId){
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> response = postLikeService.like(postId,currentUserId);
        return ResponseEntity.ok(response);
    }

    //좋아요 취소
    @PostMapping("/unlike")
    public ResponseEntity<?> unlike (@RequestParam Long postId){
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> response = postLikeService.unlike(postId,currentUserId);
        return ResponseEntity.ok(response);
    }
}
