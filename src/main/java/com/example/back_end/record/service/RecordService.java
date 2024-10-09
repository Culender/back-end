package com.example.back_end.record.service;

import com.example.back_end.domain.Record;
import com.example.back_end.domain.RecordComment;
import com.example.back_end.domain.RecordLike;
import com.example.back_end.domain.User;
import com.example.back_end.record.dto.CreateRecordDto;
import com.example.back_end.record.dto.LikedRecordDto;
import com.example.back_end.record.dto.MyRecordListDto;
import com.example.back_end.record.dto.RecordListDto;
import com.example.back_end.record.repository.RecordRepository;
import com.example.back_end.recordComment.repository.RecordCommentRepository;
import com.example.back_end.recordLike.repository.RecordLikeRepository;
import com.example.back_end.s3.S3UploadService;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordService {
    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;
    private final RecordRepository recordRepository;
    private final RecordCommentRepository recordCommentRepository;
    private final RecordLikeRepository recordLikeRepository;

    public CustomApiResponse<?> createRecord(CreateRecordDto createRecordDto, String currentUserId) {
        try{
            Optional<User> user = userRepository.findByLoginId(currentUserId);

            if(user.isEmpty()){
                CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(),"아이디가 "+currentUserId+"인 회원은 존재하지 않습니다.");
                return response;
            }

            MultipartFile imageUrl = createRecordDto.getImage();
            String imgPath = s3UploadService.upload(imageUrl,"recordImage");

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

        //댓글수 찾기
        Long commentCount = recordCommentRepository.countByRecord_RecordId(recordId);
//        log.info("commentCount"+commentCount);

        //좋아요수 찾기
        Long likeCount = recordLikeRepository.countByRecord_RecordId(recordId);

        //좋아요 여부
        Optional<RecordLike> recordLike = recordLikeRepository.findByRecord_RecordIdAndUser_UserId(recordId, findUser.get().getUserId());
        Boolean isLiked = recordLike.isPresent();

        User user = findUser.get();
        Record record = findRecord.get();

        RecordListDto recordList = new RecordListDto(
                record.getRecordId(),
                user.getProfileImg(),
                record.getUser().getNickname(),
                record.getTitle(),
                record.getDate(),
                record.getContent(),
                record.getImage(),
                isLiked,
                likeCount,
                commentCount
        );

        CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.OK.value(),recordList,"기본키가"+recordId+"인 게시물이 조회되었습니다.");
        return response;
    }

    //최신순 관람기록 조회
    public CustomApiResponse<?> getRecentRecord(String currentUserId) {
        Optional<User> findUser = userRepository.findByLoginId(currentUserId);
        if(findUser.isEmpty()){
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            return response;
        }

        List<Record> findRecords = recordRepository.findAllByOrderByCreateAtDesc();
        List<RecordListDto> recordResponse = new ArrayList<>();
        if(findRecords.isEmpty()){
            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), recordResponse,"기록한 관람기록이 존재하지 않습니다.");
        }

        User user = findUser.get();

        for(Record record : findRecords){
            //댓글수 찾기
            Long commentCount = recordCommentRepository.countByRecord_RecordId(record.getRecordId());

            //좋아요수 찾기
            Long likeCount = recordLikeRepository.countByRecord_RecordId(record.getRecordId());

            //좋아요 여부
            Optional<RecordLike> recordLike = recordLikeRepository.findByRecord_RecordIdAndUser_UserId(record.getRecordId(), findUser.get().getUserId());
            Boolean isLiked = recordLike.isPresent();

            recordResponse.add(RecordListDto.builder()
                            .recordId(record.getRecordId())
                            .profileImg(user.getProfileImg())
                            .nickname(record.getUser().getNickname())
                            .date(record.getDate())
                            .title(record.getTitle())
                            .content(record.getContent())
                            .image(record.getImage())
                            .commentCount(commentCount)
                            .likeCount(likeCount)
                            .isLiked(isLiked)
                            .build());

        }

        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), recordResponse,"최신순 관람기록이 조회되었습니다.");
    }

    //인기순 관람기록 전체조회
    public CustomApiResponse<?> getPopularRecord(String currentUserId) {
        Optional<User> findUser = userRepository.findByLoginId(currentUserId);
        if(findUser.isEmpty()){
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            return response;
        }
        List<Record> findRecords = recordRepository.findAllByOrderByLikeCountDesc();
        List<RecordListDto> recordResponse = new ArrayList<>();
        if(findRecords.isEmpty()){
            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), recordResponse,"기록한 관람기록이 존재하지 않습니다.");
        }

        User user = findUser.get();

        for(Record record : findRecords){
            //댓글수 찾기
            Long commentCount = recordCommentRepository.countByRecord_RecordId(record.getRecordId());

            //좋아요수 찾기
            Long likeCount = recordLikeRepository.countByRecord_RecordId(record.getRecordId());

            //좋아요 여부
            Optional<RecordLike> recordLike = recordLikeRepository.findByRecord_RecordIdAndUser_UserId(record.getRecordId(), findUser.get().getUserId());
            Boolean isLiked = recordLike.isPresent();

            recordResponse.add(RecordListDto.builder()
                    .recordId(record.getRecordId())
                    .profileImg(user.getProfileImg())
                    .nickname(record.getUser().getNickname())
                    .date(record.getDate())
                    .title(record.getTitle())
                    .content(record.getContent())
                    .image(record.getImage())
                    .commentCount(commentCount)
                    .likeCount(likeCount)
                    .isLiked(isLiked)
                    .build());
        }

        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), recordResponse,"인기순 관람기록이 조회되었습니다.");

    }

    public CustomApiResponse<?> getMyRecord(String currentUserId) {
        Optional<User> findUser = userRepository.findByLoginId(currentUserId);
        if(findUser.isEmpty()){
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            return response;
        }
        User user = findUser.get();
        List<Record> findRecords = recordRepository.findAllByUser_UserId(user.getUserId());
        List<MyRecordListDto> recordResponse = new ArrayList<>();

        if(findRecords.isEmpty()){
            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), recordResponse,"기록한 관람기록이 존재하지 않습니다.");
        }
        for(Record record : findRecords){
            //날짜 가공
            String Date = record.getDate(); //2024.10.3 이런 형식임
            // '.'을 기준으로 문자열을 분리
            String[] dateParts = Date.split("\\.");

            // 각각 년, 월, 일을 변수로 저장
            String year = dateParts[0];  // 2024
            String month = dateParts[1]; // 10
            String day = dateParts[2];   // 3

            recordResponse.add(MyRecordListDto.builder()
                            .recordId(record.getRecordId())
                            .year(year)
                            .month(month)
                            .day(day)
                            .build());
        }

        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), recordResponse,"나의 관람기록이 조회되었습니다.");
    }

    // 관심기록들 조회
    @Transactional(readOnly = true)
    public CustomApiResponse<?> getUserLikedRecords(String currentUserId) {

        Optional<User> findUser = userRepository.findByLoginId(currentUserId);
        if (findUser.isEmpty()) {
            return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
        }

        User user = findUser.get();

        // 사용자가 좋아요한 RecordLike 엔티티 목록을 조회
        List<RecordLike> likedRecords = recordLikeRepository.findByUser_UserId(user.getUserId());

        // 조회된 목록을 DTO로 변환하여 반환
        List<LikedRecordDto> likedRecordDtos = likedRecords.stream()
                .map(like -> new LikedRecordDto(
                        like.getRecord().getRecordId(),
                        like.getRecord().getTitle(),
                        like.getRecord().getContent(),
                        like.getRecord().getPlace(),
                        like.getRecord().getDate(),
                        like.getRecord().getImage()
                ))
                .collect(Collectors.toList());

        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), likedRecordDtos,"관심 관람기록이 조회되었습니다.");
    }
}
