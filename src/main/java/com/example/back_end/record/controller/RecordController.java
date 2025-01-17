package com.example.back_end.record.controller;

import com.example.back_end.record.dto.CreateRecordDto;
import com.example.back_end.record.dto.LikedRecordDto;
import com.example.back_end.record.service.RecordService;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.util.AuthenticationUserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;
    private final AuthenticationUserUtils userUtils;

    @PostMapping(value="/createRecord", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createRecord(@Valid @ModelAttribute CreateRecordDto createRecordDto){
        String currentUserId =userUtils.getCurrentUserId();
        CustomApiResponse<?> result = recordService.createRecord(createRecordDto,currentUserId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getRecord")
    public ResponseEntity<?> getRecord(@RequestParam Long recordId){
        String currentUserId =userUtils.getCurrentUserId();
        CustomApiResponse<?> result = recordService.getRecord(recordId,currentUserId);
        return ResponseEntity.ok(result);
    }

    //최신순 관람기록 전체 조회
    @GetMapping("/getRecentRecord")
    public ResponseEntity<?> getRecentRecord(){
        String currentUserId =userUtils.getCurrentUserId();
        CustomApiResponse<?> result = recordService.getRecentRecord(currentUserId);
        return ResponseEntity.ok(result);
    }

    //인기순 (좋아요순) 관람기록 전체 조회
    @GetMapping("/getPopularRecord")
    public ResponseEntity<?> getPopularRecord(){
        String currentUserId =userUtils.getCurrentUserId();
        CustomApiResponse<?> result = recordService.getPopularRecord(currentUserId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getMyRecord")
    public ResponseEntity<?> getMyRecord(){
        String currentUserId =userUtils.getCurrentUserId();
        CustomApiResponse<?> result = recordService.getMyRecord(currentUserId);
        return ResponseEntity.ok(result);
    }

    // 사용자가 좋아요한 관람기록 조회
    @GetMapping("/getLikedRecords")
    public ResponseEntity<?> getUserLikedRecords() {
        String currentUserId = userUtils.getCurrentUserId();
        CustomApiResponse<?> result = recordService.getUserLikedRecords(currentUserId);
        return ResponseEntity.ok(result);
    }
}
