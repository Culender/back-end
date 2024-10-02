package com.example.back_end.record.service;

import com.example.back_end.domain.Record;
import com.example.back_end.domain.User;
import com.example.back_end.record.dto.CreateRecordDto;
import com.example.back_end.record.dto.RecordListDto;
import com.example.back_end.record.repository.RecordRepository;
import com.example.back_end.s3.S3UploadService;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;
    private final RecordRepository recordRepository;

    public CustomApiResponse<?> createRecord(CreateRecordDto createRecordDto, String currentUserId) {
        try{
            Optional<User> user = userRepository.findByLoginId(currentUserId);

            if(user.isEmpty()){
                CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(),"아이디가 "+currentUserId+"인 회원은 존재하지 않습니다.");
                return response;
            }

            MultipartFile imageUrl = createRecordDto.getImage();
            String imgPath = s3UploadService.upload(imageUrl,"postImage");

            //엔티티 생성
            Record record = Record.toEntity(createRecordDto,user.get(),imgPath);
            recordRepository.save(record); //DB에 저장

            CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null,"관람 기록이 작성되었습니다.");
            return response;

        } catch(DataAccessException dae) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }

    //TODO : 댓글수, 좋아요수 추가 필요
    public CustomApiResponse<?> getRecord(Long recordId, String currentUserId) {

        //존재하는 유저인지
        Optional<User> findUser = userRepository.findByLoginId(currentUserId);
        if(findUser.isEmpty()){
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            return response;
        }

        //해당 게시물이 존재하는지 확인
        Optional<Record> findRecord = recordRepository.findById(recordId);
        if(findRecord.isEmpty()){
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(),"기본키가 "+recordId+"인 관람기록은 존재하지 않습니다.");
            return response;
        }

        User user = findUser.get();
        Record record = findRecord.get();

        RecordListDto recordList = new RecordListDto(
                record.getRecordId(),
                user.getNickname(),
                record.getDate(),
                record.getContent(),
                record.getImage()
        );

        CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.OK.value(),recordList,"기본키가"+recordId+"인 게시물이 조회되었습니다.");
        return response;
    }
}
