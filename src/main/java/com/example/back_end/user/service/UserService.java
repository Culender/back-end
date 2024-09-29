package com.example.back_end.user.service;

import com.example.back_end.domain.User;
import com.example.back_end.user.dto.SignInReqDto;
import com.example.back_end.user.dto.SignInResDto;
import com.example.back_end.user.dto.SignUpDto;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public String signUp(SignUpDto dto) {
        //이미 존재하는 아이디인지 확인
        Optional<User> findUser = userRepository.findByLoginId(dto.getLoginId());
        if(findUser.isPresent()){ //존재하는 아이디인 경우
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        //유저 엔티티 생성
        User user = User.toEntity(dto);
        userRepository.save(user); //DB에 저장

        return "회원가입에 성공하였습니다.";
    }

    public CustomApiResponse<?> signIn(SignInReqDto dto) {
        User user = userRepository.findByLoginId(dto.getLoginId()).orElseThrow(RuntimeException::new);

        //토큰
        String token = jwtTokenProvider.createToken(user.getLoginId());

        SignInResDto resToken = SignInResDto.builder().accessToken(token).build();

        //응답 형식
        CustomApiResponse<?> response = CustomApiResponse.createSuccess(200,resToken,"로그인에 성공하였습니다.");

        return response;
    }
}
