package com.example.back_end.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileDto {
    private String nickname;   // 수정할 닉네임
    private MultipartFile profileImg; // 수정할 프로필 이미지 경로
}
