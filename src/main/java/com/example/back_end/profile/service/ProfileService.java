package com.example.back_end.profile.service;

import com.example.back_end.domain.User;
import com.example.back_end.profile.dto.GetProfileDto;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    public CustomApiResponse<?> getUserProfile(String currentUserId) {
        Optional<User> user = userRepository.findByLoginId(currentUserId);

        if(user.isEmpty()){
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(),"아이디가 "+currentUserId+"인 회원은 존재하지 않습니다.");
            return response;
        }
        GetProfileDto getProfileDto = new GetProfileDto (
                user.get().getNickname(),
                user.get().getProfileImg()
        );

        CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.OK.value(),getProfileDto,"로그인 아이디가 "+currentUserId+"인 프로필이 조회되었습니다.");
        return response;
    }
}
