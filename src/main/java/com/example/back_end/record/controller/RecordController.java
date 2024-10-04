package com.example.back_end.record.controller;

import com.example.back_end.record.dto.CreateRecordDto;
import com.example.back_end.record.service.RecordService;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.util.AuthenticationUserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
