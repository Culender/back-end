package com.example.back_end.post.service;

import com.example.back_end.domain.Post;
import com.example.back_end.domain.PostLike;
import com.example.back_end.domain.User;
import com.example.back_end.post.repository.PostLikeRepository;
import com.example.back_end.post.repository.PostRepository;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    //좋아요
    public CustomApiResponse<?> like(Long postId, String currentUserId) {
        try{
            Optional<User> user = userRepository.findByLoginId(currentUserId);
            Optional<Post> post = postRepository.findById(postId);

            if(user.isEmpty()){
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            }
            if(post.isEmpty()){
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 게시글입니다.");
            }

            // 중복 좋아요 여부 확인
            Optional<PostLike> findPostLike = postLikeRepository.findByPost_PostIdAndUser_UserId(post.get().getPostId(), user.get().getUserId());
            if(findPostLike.isPresent()){
                return CustomApiResponse.createFailWithout(HttpStatus.CONFLICT.value(), "이미 좋아요를 눌렀습니다.");
            }

            //좋아요 생성
            PostLike postLike = PostLike.toEntity(user.get(),post.get());
            postLikeRepository.save(postLike);

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(),null,"좋아요");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }

    //좋아요 취소
    public CustomApiResponse<?> unlike(Long postId, String currentUserId) {
        try{
            Optional<User> user = userRepository.findByLoginId(currentUserId);
            Optional<Post> post = postRepository.findById(postId);

            if(user.isEmpty()){
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            }
            if(post.isEmpty()){
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 게시글입니다.");
            }

            // 중복 좋아요 취소 여부 확인
            Optional<PostLike> findRecordLike = postLikeRepository.findByPost_PostIdAndUser_UserId(post.get().getPostId(), user.get().getUserId());
            if(findRecordLike.isEmpty()){
                return CustomApiResponse.createFailWithout(HttpStatus.CONFLICT.value(), "이미 좋아요를 취소하였습니다.");
            }
                //좋아요 삭제
                postLikeRepository.delete(findRecordLike.get());
                return CustomApiResponse.createSuccess(HttpStatus.OK.value(),null,"좋아요 취소");

            } catch (Exception e) {
                return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
            }
    }
}
