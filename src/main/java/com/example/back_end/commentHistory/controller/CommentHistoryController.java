package com.example.back_end.commentHistory.controller;

import com.example.back_end.commentHistory.dto.CommentHistoryDto;
import com.example.back_end.commentHistory.service.CommentHistoryService;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.util.AuthenticationUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/CommentHistory")
@RequiredArgsConstructor
public class CommentHistoryController {

    private final CommentHistoryService commentHistoryService;
    private final AuthenticationUserUtils userUtils;

    // 댓글 이력 불러오기
    @GetMapping("/getComments")
    public ResponseEntity<?> getUserComments() {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = commentHistoryService.getCommentHistoryByUser(currentUserId);
        return ResponseEntity.ok(result);
    }
}
