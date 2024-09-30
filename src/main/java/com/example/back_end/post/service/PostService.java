package com.example.back_end.post.service;

import com.example.back_end.domain.Post;
import com.example.back_end.domain.User;
import com.example.back_end.post.dto.CreatePostDto;
import com.example.back_end.post.repository.PostRepository;
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
public class PostService {
    private final UserRepository userRepository;
    private final S3UploadService s3UploadService;
    private final PostRepository postRepository;

    public CustomApiResponse<?> createPost(CreatePostDto createPostDto, String currentUserId) {
        try{
            Optional<User> user = userRepository.findByLoginId(currentUserId);

            if(user.isEmpty()){
                CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(),"아이디가 "+currentUserId+"인 회원은 존재하지 않습니다.");
                return response;
            }

            MultipartFile imageUrl = createPostDto.getImage();
            String imgPath = s3UploadService.upload(imageUrl,"postImage");

            //엔티티 생성
            Post post = Post.toEntity(createPostDto,user.get(),imgPath);
            postRepository.save(post); //DB에 저장

            CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null,"관람 기록이 작성되었습니다.");
            return response;

        } catch(DataAccessException dae) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        }


    }
}
