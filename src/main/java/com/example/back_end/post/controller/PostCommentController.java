package com.example.back_end.post.controller;

import com.example.back_end.post.dto.CreateCommentDto;
import com.example.back_end.post.dto.UpdateCommentDto;
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
    @PostMapping(value="/createComment")
    public ResponseEntity<?> createPost(@RequestBody @Valid CreateCommentDto createCommentDto) {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postCommentService.createComment(createCommentDto, currentUserId);
        return ResponseEntity.ok(result);
    }

    // 댓글 조회
    @GetMapping("/getCommentsByPost")
    public ResponseEntity<?> getCommentsByPost(@RequestParam Long postId){
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postCommentService.getCommentsByPost(postId, currentUserId);
        return ResponseEntity.ok(result);
    }

    // 댓글 수정
    @PatchMapping(value = "/updateComment")
    public ResponseEntity<?> updateComment(@RequestParam Long commentId, @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postCommentService.updateComment(commentId, updateCommentDto, currentUserId);
        return ResponseEntity.ok(result);
    }

    // 댓글 삭제
    @DeleteMapping("/deleteComment")
    public ResponseEntity<?> deleteComment(@RequestParam Long commentId) {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = postCommentService.deleteComment(commentId, currentUserId);
        return ResponseEntity.ok(result);
    }
}
