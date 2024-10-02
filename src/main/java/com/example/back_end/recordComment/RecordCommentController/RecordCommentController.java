package com.example.back_end.recordComment.RecordCommentController;

import com.example.back_end.recordComment.dto.CreateRecordCommentDto;
import com.example.back_end.recordComment.service.RecordCommentService;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.util.AuthenticationUserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recordComment")
@RequiredArgsConstructor
public class RecordCommentController {
    private final AuthenticationUserUtils userUtils;
    private final RecordCommentService recordCommentService;

    //댓글 작성
    @PostMapping("/createRecordComment")
    public ResponseEntity<?> createRecordComment(@RequestBody @Valid CreateRecordCommentDto recordCommentDto){
        String currentUser = userUtils.getCurrentUserId();
        CustomApiResponse<?> response = recordCommentService.createRecordComment(recordCommentDto,currentUser);
        return ResponseEntity.ok(response);
    }

    //댓글 조회
    @GetMapping("/getRecordCommentList")
    public ResponseEntity<?> getRecordCommentList(@RequestParam Long recordId){
        String currentUser = userUtils.getCurrentUserId();
        CustomApiResponse<?> response = recordCommentService.getRecordCommentList(recordId,currentUser);
        return ResponseEntity.ok(response);
    }
}
