package com.example.back_end.commentHistory.service;

import com.example.back_end.commentHistory.dto.CommentHistoryDto;
import com.example.back_end.domain.PostComment;
import com.example.back_end.domain.RecordComment;
import com.example.back_end.domain.User;
import com.example.back_end.post.repository.PostCommentRepository;
import com.example.back_end.recordComment.repository.RecordCommentRepository;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentHistoryService {

    private final PostCommentRepository postCommentRepository;
    private final RecordCommentRepository recordCommentRepository;
    private final UserRepository userRepository;

    public CustomApiResponse<?> getCommentHistoryByUser(String currentUserId) {
        Optional<User> findUser = userRepository.findByLoginId(currentUserId);
        if(findUser.isEmpty()){
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            return response;
        }
        User user = findUser.get();
        List<CommentHistoryDto> commentHistory = new ArrayList<>();

        // Post comments
        List<PostComment> postComments = postCommentRepository.findByUser_UserId(user.getUserId());
        postComments.forEach(comment -> commentHistory.add(
                CommentHistoryDto.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .nickname(comment.getUser().getNickname())
                        .createdAt(comment.getCreateAt())
                        .type("Post")  // Post type으로 구분
                        .build()
        ));

        // Record comments
        List<RecordComment> recordComments = recordCommentRepository.findByUser_UserId(user.getUserId());
        recordComments.forEach(comment -> commentHistory.add(
                CommentHistoryDto.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .nickname(comment.getUser().getNickname())
                        .createdAt(comment.getCreateAt()) //recordComment에서 extends BaseEntity가 없음
                        .type("Record")  // Record type으로 구분
                        .build()
        ));

        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), commentHistory, "댓글 이력을 불러왔습니다.");
    }
}
