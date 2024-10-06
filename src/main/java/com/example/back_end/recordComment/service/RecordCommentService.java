package com.example.back_end.recordComment.service;

import com.example.back_end.domain.Record;
import com.example.back_end.domain.RecordComment;
import com.example.back_end.domain.User;
import com.example.back_end.record.repository.RecordRepository;
import com.example.back_end.recordComment.dto.CreateRecordCommentDto;
import com.example.back_end.recordComment.dto.RecordCommentListDto;
import com.example.back_end.recordComment.repository.RecordCommentRepository;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public CustomApiResponse<?> getRecordCommentList(Long recordId, String currentUser) {
        //존재하는 관람 기록인지 확인
        Optional<Record> findRecord = recordRepository.findById(recordId);
        if (findRecord.isEmpty()) {
            return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 게시물입니다.");
        }

        //해당 recordId를 가진 관람 기록의 댓글 찾기
        List<RecordComment> recordComments = recordCommentRepository.findByRecord_RecordId(recordId);
        List<RecordCommentListDto> recordCommentListDto = new ArrayList<>();

        //RecordComment 객체를 RecordCommentListDto로 변환하여 리스트에 추가
        for (RecordComment recordComment : recordComments) {
            RecordCommentListDto dto = new RecordCommentListDto(
                    recordComment.getUser().getNickname(),
                    recordComment.getContent(),
                    recordComment.getUser().getProfileImg()
            );
            recordCommentListDto.add(dto);
        }

        //댓글 리스트가 비어 있는 경우
        if (recordComments.isEmpty()) {
            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "댓글이 없습니다.");
        }

        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), recordCommentListDto, "댓글이 조회되었습니다.");
    }

}
