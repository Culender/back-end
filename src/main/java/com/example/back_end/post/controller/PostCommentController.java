package com.example.back_end.post.controller;

import com.example.back_end.post.dto.CreateCommentDto;
import com.example.back_end.post.dto.CreatePostDto;
import com.example.back_end.post.dto.UpdateCommentDto;
import com.example.back_end.post.dto.UpdatePostDto;
import com.example.back_end.post.service.PostCommentService;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.util.AuthenticationUserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/postComment")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final AuthenticationUserUtils userUtils;

    // 댓글 작성
    @PostMapping(value="/createComment", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createPost(@Valid @ModelAttribute CreateCommentDto createCommentDto) {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postCommentService.createComment(createCommentDto, currentUserId);
        return ResponseEntity.ok(result);
    }

    // 댓글 조회
    @GetMapping("/getCommentsByPost")
    public ResponseEntity<?> getPost(@RequestParam Long postId){
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postCommentService.getCommentsByPost(postId, currentUserId);
        return ResponseEntity.ok(result);
    }

    // 댓글 수정
    @PatchMapping(value = "/updateComment", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updatePost(@RequestParam Long postId, @Valid @ModelAttribute UpdateCommentDto updateCommentDto) {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postCommentService.updateComment(postId, updateCommentDto, currentUserId);
        return ResponseEntity.ok(result);
    }

    // 댓글 삭제
    @DeleteMapping("/deleteComment")
    public ResponseEntity<?> deletePost(@RequestParam Long postId) {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postCommentService.deleteComment(postId, currentUserId);
        return ResponseEntity.ok(result);
    }
}
