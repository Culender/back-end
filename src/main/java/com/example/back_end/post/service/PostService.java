package com.example.back_end.post.service;

import com.example.back_end.domain.Post;
import com.example.back_end.domain.PostLike;
import com.example.back_end.domain.User;
import com.example.back_end.post.dto.CreatePostDto;
import com.example.back_end.post.dto.GetPostDto;
import com.example.back_end.post.dto.LikedPostDto;
import com.example.back_end.post.dto.UpdatePostDto;
import com.example.back_end.post.repository.PostCommentRepository;
import com.example.back_end.post.repository.PostLikeRepository;
import com.example.back_end.post.repository.PostRepository;
import com.example.back_end.s3.S3UploadService;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostLikeRepository postLikeRepository;
    private final S3UploadService s3UploadService;

    // 게시글 작성
    public CustomApiResponse<?> createPost(CreatePostDto createPostDto, String currentUserId) {
        try {
            Optional<User> findUser = userRepository.findByLoginId(currentUserId);

            if (findUser.isEmpty()) {
                CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "아이디가 " + currentUserId + "인 회원은 존재하지 않습니다.");
                return response;
            }

            MultipartFile imageUrl = createPostDto.getImage();
            String imgPath = s3UploadService.upload(imageUrl, "postImage");

            //게시글 엔티티 생성
            Post post = Post.toEntity(createPostDto, findUser.get(), imgPath);
            postRepository.save(post); // 게시글 엔티티 DB에 저장

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), null,"게시글 작성이 완료되었습니다.");
        }
        catch(DataAccessException dae) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public CustomApiResponse<?> getAllPosts(String currentUserId) {
        try {
            // 유저 존재 여부 확인
            Optional<User> findUser = userRepository.findByLoginId(currentUserId);
            if (findUser.isEmpty()) {
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            }

            // 모든 게시글을 조회
            List<Post> posts = postRepository.findAll();
            if (posts.isEmpty()) {
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "게시글이 존재하지 않습니다.");
            }

            // 각 게시글을 DTO로 변환하여 리스트로 반환
            List<GetPostDto> postDtos = posts.stream().map(post -> {
                Long commentCount = postCommentRepository.countByPost_PostId(post.getPostId()); //댓글 개수 가져오기
                Long likeCount = postLikeRepository.countByPost_PostId(post.getPostId()); //좋아요 개수 가져오기
                Optional<PostLike> postLike = postLikeRepository.findByPost_PostIdAndUser_UserId(post.getPostId(), findUser.get().getUserId());
                Boolean isLiked = postLike.isPresent();

                return GetPostDto.builder()  // 빌더 패턴 사용
                        .postId(post.getPostId())
                        .title(post.getTitle())
                        .nickname(post.getUser().getNickname())
                        .category(post.getCategory())
                        .content(post.getContent())
                        .image(post.getImage())
                        .createdAt(post.getCreateAt())
                        .updatedAt(post.getUpdateAt())
                        .commentCount(commentCount)
                        .likeCount(likeCount)
                        .isLiked(isLiked)
                        .build();
            }).collect(Collectors.toList());

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), postDtos, "전체 게시글 조회가 완료되었습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }

    // 게시글 조회
    @Transactional(readOnly = true)
    public CustomApiResponse<?> getPost(Long postId, String currentUserId) {
        try {
            // 존재하는 유저인지 검사(로그인 유저만 볼 수 있도록)
            Optional<User> findUser = userRepository.findByLoginId(currentUserId);
            if(findUser.isEmpty()){
                CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
                return response;
            }

            Optional<Post> findPost = postRepository.findById(postId);

            if (findPost.isEmpty()) { // 해당하는 게시글이 없을 경우
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 게시글이 존재하지 않습니다.");
            }

            //댓글수 찾기
            Long commentCount = postCommentRepository.countByPost_PostId(postId);

            //좋아요수 찾기
            Long likeCount = postLikeRepository.countByPost_PostId(postId);

            //좋아요 여부
            Optional<PostLike> postLike = postLikeRepository.findByPost_PostIdAndUser_UserId(postId, findUser.get().getUserId());
            Boolean isLiked = postLike.isPresent();

            User user = findUser.get();
            Post post = findPost.get();

            GetPostDto getPost = GetPostDto.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .nickname(user.getNickname())
                    .category(post.getCategory())
                    .content(post.getContent())
                    .image(post.getImage())
                    .createdAt(post.getCreateAt())
                    .updatedAt(post.getUpdateAt())
                    .commentCount(commentCount)
                    .likeCount(likeCount)
                    .isLiked(isLiked)
                    .build();

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), getPost, "게시글 조회가 완료되었습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }

    // 게시글 수정
    public CustomApiResponse<?> updatePost(Long postId, UpdatePostDto updatePostDto, String currentUserId) {
        try {
            Optional<Post> post = postRepository.findById(postId);

            if (post.isEmpty()) {
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 게시글이 존재하지 않습니다.");
            }

            // 작성자 확인
            if (!post.get().getUser().getLoginId().equals(currentUserId)) {
                return CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(), "게시글을 수정할 권한이 없습니다.");
            }

            // 이미지가 있으면 업로드
            MultipartFile newImage = updatePostDto.getImage();
            String imgPath = post.get().getImage(); // 기존 이미지 경로

            if (newImage != null && !newImage.isEmpty()) {
                imgPath = s3UploadService.upload(newImage, "postImage");
            }

            // 게시글 정보 업데이트
            post.get().updatePost(updatePostDto.getTitle(), updatePostDto.getContent(), imgPath);
            postRepository.save(post.get());

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "게시글이 성공적으로 수정되었습니다.");
        } catch (DataAccessException dae) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }

    // 게시글 삭제
    public CustomApiResponse<?> deletePost(Long postId, String currentUserId) {
        try {
            Optional<Post> post = postRepository.findById(postId);

            if (post.isEmpty()) {
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 게시글이 존재하지 않습니다.");
            }

            // 작성자 확인
            if (!post.get().getUser().getLoginId().equals(currentUserId)) {
                return CustomApiResponse.createFailWithout(HttpStatus.FORBIDDEN.value(), "게시글을 삭제할 권한이 없습니다.");
            }

            postRepository.delete(post.get()); // 게시글 삭제

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), null, "게시글이 성공적으로 삭제되었습니다.");
        } catch (DataAccessException dae) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }


    // 주제별 게시글 조회
    @Transactional(readOnly = true)
    public CustomApiResponse<?> getPostsByCategory(String category, String currentUserId) {
        try {
            // 유저 존재 여부 확인
            Optional<User> findUser = userRepository.findByLoginId(currentUserId);
            if (findUser.isEmpty()) {
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            }

            // 주제(category)에 따른 게시글을 조회
            List<Post> posts = postRepository.findByCategory(category);
            if (posts.isEmpty()) {
                return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 주제에 게시글이 존재하지 않습니다.");
            }

            // 각 게시글을 DTO로 변환하여 리스트로 반환
            List<GetPostDto> postDtos = posts.stream().map(post -> {
                Long commentCount = postCommentRepository.countByPost_PostId(post.getPostId()); //댓글 개수 가져오기
                Long likeCount = postLikeRepository.countByPost_PostId(post.getPostId()); //좋아요 개수 가져오기
                Optional<PostLike> postLike = postLikeRepository.findByPost_PostIdAndUser_UserId(post.getPostId(), findUser.get().getUserId());
                Boolean isLiked = postLike.isPresent();

                return GetPostDto.builder()
                        .postId(post.getPostId())
                        .title(post.getTitle())
                        .nickname(post.getUser().getNickname())
                        .category(post.getCategory())
                        .content(post.getContent())
                        .image(post.getImage())
                        .createdAt(post.getCreateAt())
                        .updatedAt(post.getUpdateAt())
                        .commentCount(commentCount)
                        .likeCount(likeCount)
                        .isLiked(isLiked)
                        .build();
            }).collect(Collectors.toList());

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), postDtos, "주제별 게시글 조회가 완료되었습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }

    // 관심 게시글 조회 메서드
    public CustomApiResponse<?> getLikedPosts(String currentUserId) {

        Optional<User> findUser = userRepository.findByLoginId(currentUserId);
        if(findUser.isEmpty()){
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            return response;
        }

        // 사용자가 좋아요한 게시글 목록을 가져옴
        List<PostLike> likedPosts = postLikeRepository.findByUser_UserId(findUser.get().getUserId());
        // 빈 리스트인지 확인
        if (likedPosts.isEmpty()) {
            // 반환되는 목록이 없는 경우에 대한 처리
            return CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "관심 게시글이 없습니다.");
        }

        // 게시글 DTO로 변환
        List<LikedPostDto> likedPostDtos = likedPosts.stream()
                .map(like -> {
                    Post post = like.getPost(); // 좋아요한 게시글을 가져옴
                    return LikedPostDto.builder()
                            .postId(post.getPostId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .nickname(post.getUser().getNickname())
                            .image(post.getImage())
                            .build();
                })
                .collect(Collectors.toList());

        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), likedPostDtos, "관심 게시글 목록이 조회되었습니다.");
    }

}
