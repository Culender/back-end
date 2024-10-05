package com.example.back_end.post.controller;

import com.example.back_end.post.dto.CreatePostDto;
import com.example.back_end.post.dto.UpdatePostDto;
import com.example.back_end.post.service.PostService;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.util.AuthenticationUserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AuthenticationUserUtils userUtils;

    // 게시글 작성
    @PostMapping(value="/createPost", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createPost(@Valid @ModelAttribute CreatePostDto createPostDto) {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postService.createPost(createPostDto, currentUserId);
        return ResponseEntity.ok(result);
    }

    // 게시글 전체 조회
    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts() {
        String currentUserId = userUtils.getCurrentUserId();
        return ResponseEntity.ok(postService.getAllPosts(currentUserId));
    }

    // 게시글 조회
    @GetMapping("/getPost")
    public ResponseEntity<?> getPost(@RequestParam Long postId){
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postService.getPost(postId, currentUserId);
        return ResponseEntity.ok(result);
    }

    // 게시글 수정
    @PatchMapping(value = "/updatePost", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updatePost(@RequestParam Long postId, @Valid @ModelAttribute UpdatePostDto updatePostDto) {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postService.updatePost(postId, updatePostDto, currentUserId);
        return ResponseEntity.ok(result);
    }

    // 게시글 삭제
    @DeleteMapping("/deletePost")
    public ResponseEntity<?> deletePost(@RequestParam Long postId) {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postService.deletePost(postId, currentUserId);
        return ResponseEntity.ok(result);
    }

    // 주제별 게시글 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getPostsByCategory(@PathVariable String category) {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> response = postService.getPostsByCategory(category, currentUserId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
