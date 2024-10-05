package com.example.back_end.post.service;

import com.example.back_end.domain.Post;
import com.example.back_end.domain.User;
import com.example.back_end.post.dto.CreatePostDto;
import com.example.back_end.post.dto.GetPostDto;
import com.example.back_end.post.dto.UpdatePostDto;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final S3UploadService s3UploadService;

    public CustomApiResponse<?> createPost(CreatePostDto createPostDto, String currentUserId) {
        try {
            Optional<User> user = userRepository.findByLoginId(currentUserId);

            if (user.isEmpty()) {
                CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "아이디가 " + currentUserId + "인 회원은 존재하지 않습니다.");
                return response;
            }

            MultipartFile imageUrl = createPostDto.getImage();
            String imgPath = s3UploadService.upload(imageUrl, "postImage");

            //게시글 엔티티 생성
            Post post = Post.toEntity(createPostDto,user.get(), imgPath);
            postRepository.save(post); // 게시글 엔티티 DB에 저장

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), null,"게시글 작성이 완료되었습니다.");
        }
        catch(DataAccessException dae) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }

    @Transactional(readOnly = true)
    // 게시글 조회 메서드
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

            User user = findUser.get();
            Post post = findPost.get();

            GetPostDto getPost = new GetPostDto(
                    post.getPostId(),
                    post.getTitle(),
                    user.getNickname(),
                    post.getCategory(),
                    post.getContent(),
                    post.getImage(),
                    post.getCreateAt(),
                    post.getUpdateAt()
            );

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), getPost, "게시글 조회가 완료되었습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }
    }

    // 게시글 수정 메서드
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

    // 게시글 삭제 메서드
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

}
