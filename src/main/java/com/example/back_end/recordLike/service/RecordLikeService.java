package com.example.back_end.recordLike.service;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.amazonaws.services.kms.model.NotFoundException;
import com.example.back_end.domain.Record;
import com.example.back_end.domain.RecordLike;
import com.example.back_end.domain.User;
import com.example.back_end.record.repository.RecordRepository;
import com.example.back_end.recordLike.repository.RecordLikeRepository;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class RecordLikeService {

    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final RecordLikeRepository recordLikeRepository;

    public RecordLikeService(UserRepository userRepository, RecordRepository recordRepository, RecordLikeRepository recordLikeRepository) {
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
        this.recordLikeRepository = recordLikeRepository;
    }

    //좋아요
    public CustomApiResponse<?> like(Long recordId, String currentUserId) {
        Optional<User> user = userRepository.findByLoginId(currentUserId);
        Optional<Record> record = recordRepository.findById(recordId);
        if(user.isEmpty()){
            return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
        }
        if(record.isEmpty()){
            return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 관람기록입니다.");
        }

        // 중복 좋아요 여부 확인
        Optional<RecordLike> findRecordLike = recordLikeRepository.findByRecord_RecordIdAndUser_UserId(record.get().getRecordId(), user.get().getUserId());
        if(findRecordLike.isPresent()){
            return CustomApiResponse.createFailWithout(HttpStatus.CONFLICT.value(), "이미 좋아요를 눌렀습니다.");
        }

        //좋아요 생성
        RecordLike recordLike = RecordLike.toEntity(user.get(),record.get());
        recordLikeRepository.save(recordLike);

        return CustomApiResponse.createSuccess(HttpStatus.OK.value(),null,"좋아요");
    }
}
