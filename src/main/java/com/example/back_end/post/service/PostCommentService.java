package com.example.back_end.post.service;

import com.example.back_end.domain.Post;
import com.example.back_end.domain.PostComment;
import com.example.back_end.domain.User;
import com.example.back_end.post.dto.CreateCommentDto;
import com.example.back_end.post.dto.GetCommentDto;
import com.example.back_end.post.dto.UpdateCommentDto;
import com.example.back_end.post.repository.PostCommentRepository;
import com.example.back_end.post.repository.PostRepository;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommentService {

    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    // 댓글 작성
    public CustomApiResponse<?> createComment(CreateCommentDto createCommentDto, String currentUserId) {
        Optional<Post> post = postRepository.findById(createCommentDto.getPostId());
        if (post.isEmpty()) {
            return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 게시글을 찾을 수 없습니다.");
        }

        Optional<User> user = userRepository.findByLoginId(currentUserId);
        if (user.isEmpty()) {
            return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 사용자를 찾을 수 없습니다.");
        }
         // 댓글 엔티티 생성
            PostComment comment = PostComment.toEntity(createCommentDto, user.get(), post.get());

            postCommentRepository.save(comment); // 댓글 저장
            System.out.println("댓글 저장 완료, commentId:" + comment.getCommentId());

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "댓글 작성이 완료되었습니다.");
    }

    @Transactional(readOnly = true)
    // 해당 게시글의 댓글 조회
    public CustomApiResponse<?> getCommentsByPost(Long postId, String currentUserId) {
        try {
            // 존재하는 유저인지 검사(로그인 유저만 볼 수 있도록)
            Optional<User> findUser = userRepository.findByLoginId(currentUserId);
            if(findUser.isEmpty()){
                CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
                return response;
            }

            Optional<Post> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 게시글을 찾을 수 없습니다.");
            }

            // 해당 게시글에 달린 댓글 리스트를 조회
            List<GetCommentDto> commentDtos = postCommentRepository.findByPost_PostId(postId).stream()
                    .map(comment -> GetCommentDto.builder()
                            .commentId(comment.getCommentId())
                            .nickname(comment.getUser().getNickname())
                            .content(comment.getContent())
                            .createdAt(comment.getCreateAt())
                            .updatedAt(comment.getUpdateAt())
                            .build())
                    .collect(Collectors.toList()); // 리스트로 수집

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), commentDtos, "댓글 목록을 불러왔습니다.");
    }
        catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }

    // 댓글 수정
    public CustomApiResponse<?> updateComment(Long commentId, UpdateCommentDto updateCommentDto, String currentUserId) {
        try {
            Optional<PostComment> comment = postCommentRepository.findById(commentId);
            if (comment.isEmpty()) {
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 댓글을 찾을 수 없습니다.");
            }

            if (!comment.get().getUser().getLoginId().equals(currentUserId)) {
                return CustomApiResponse.createFailWithout(HttpStatus.UNAUTHORIZED.value(), "해당 댓글을 수정할 권한이 없습니다.");
            }

            // 댓글 내용 업데이트
            comment.get().update(updateCommentDto.getContent());
            postCommentRepository.save(comment.get());

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "댓글 수정이 완료되었습니다.");
        } catch (DataAccessException dae) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }

    // 댓글 삭제
    public CustomApiResponse<?> deleteComment(Long commentId, String currentUserId) {
        try {
            Optional<PostComment> comment = postCommentRepository.findById(commentId);
            if (comment.isEmpty()) {
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 댓글을 찾을 수 없습니다.");
            }

            if (!comment.get().getUser().getLoginId().equals(currentUserId)) {
                return CustomApiResponse.createFailWithout(HttpStatus.UNAUTHORIZED.value(), "해당 댓글을 삭제할 권한이 없습니다.");
            }

            // 댓글 삭제
            postCommentRepository.delete(comment.get());

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "댓글이 삭제되었습니다.");
        }
        catch(DataAccessException dae) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }
}
