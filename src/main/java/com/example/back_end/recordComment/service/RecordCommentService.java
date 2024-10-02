package com.example.back_end.recordComment.service;

import com.example.back_end.domain.Record;
import com.example.back_end.domain.RecordComment;
import com.example.back_end.domain.User;
import com.example.back_end.record.repository.RecordRepository;
import com.example.back_end.recordComment.dto.CreateRecordCommentDto;
import com.example.back_end.recordComment.repository.RecordCommentRepository;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordCommentService {
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final RecordCommentRepository recordCommentRepository;

    @Transactional
    public CustomApiResponse<?> createRecordComment (CreateRecordCommentDto dto, String currentUser){
        //존재하는 유저인지
        Optional<User> findUser = userRepository.findByLoginId(currentUser);
        if(findUser.isEmpty()){
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            return response;
        }
        //존재하는 관람기록인지
        Optional<Record> findRecord = recordRepository.findById(dto.getRecordId());
        if(findRecord.isEmpty()){
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 관람기록입니다.");
            return response;
        }

        User user = findUser.get();
        Record record = findRecord.get();

        //댓글 엔티티 생성
        RecordComment recordComment = RecordComment.toEntity(
                dto,
                user,
                record
        );
        recordCommentRepository.save(recordComment); //DB에 저장

        CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "댓글 작성이 완료되었습니다.");
        return response;
    }

}
