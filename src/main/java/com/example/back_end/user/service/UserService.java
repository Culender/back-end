package com.example.back_end.user.service;

import com.example.back_end.domain.User;
import com.example.back_end.user.dto.SignUpDto;
import com.example.back_end.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
}
