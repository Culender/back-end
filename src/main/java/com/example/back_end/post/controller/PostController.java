package com.example.back_end.post.controller;

import com.example.back_end.post.dto.CreatePostDto;
import com.example.back_end.post.service.PostService;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.util.AuthenticationUserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AuthenticationUserUtils userUtils;

    @PostMapping(value="/createPost", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createPost(@Valid @ModelAttribute CreatePostDto createPostDto){
        String currentUserId =userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postService.createPost(createPostDto,currentUserId);
        return ResponseEntity.ok(result);
    }
}
