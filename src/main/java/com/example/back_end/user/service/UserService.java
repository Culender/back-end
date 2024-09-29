package com.example.back_end.user.service;

import com.example.back_end.domain.User;
import com.example.back_end.user.dto.SignInReqDto;
import com.example.back_end.user.dto.SignInResDto;
import com.example.back_end.user.dto.SignUpDto;
import com.example.back_end.user.repository.UserRepository;
import com.example.back_end.util.response.CustomApiResponse;
import com.example.back_end.util.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public CustomApiResponse<?> signUp(SignUpDto dto) {
        //이미 존재하는 아이디인지 확인
        Optional<User> findUser = userRepository.findByLoginId(dto.getLoginId());
        if(findUser.isPresent()){ //존재하는 아이디인 경우
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.CONFLICT.value(), "이미 존재하는 아이디입니다.");
            return response;
        }

        String encodePassword = passwordEncoder.encode(dto.getPassword()); //비밀번호 암호화

        //유저 엔티티 생성
        User user = User.toEntity(dto,encodePassword);
        userRepository.save(user); //DB에 저장
        CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.OK.value(), null,"회원가입에 성공하였습니다.");

        return response;
    }

    //로그인
    @Transactional
    public CustomApiResponse<?> signIn(SignInReqDto dto) {
            //존재하지 않는 사용자
             Optional<User> user = userRepository.findByLoginId(dto.getLoginId());
             if(user.isEmpty()){
                 CustomApiResponse<Object> failResponse = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 사용자입니다.");
                 return failResponse;
             }

            //비밀번호 확인
            if(!passwordEncoder.matches(dto.getPassword(),user.get().getPassword())){
                CustomApiResponse<Object> failResponse = CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다.");
                return failResponse;
            }

            //토큰
            String token = jwtTokenProvider.createToken(user.get().getLoginId());

            SignInResDto resToken = SignInResDto.builder().accessToken(token).build();

            //응답 형식
            CustomApiResponse<?> response = CustomApiResponse.createSuccess(200,resToken,"로그인에 성공하였습니다.");

            return response;
    }
}
