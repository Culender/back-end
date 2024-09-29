package com.example.back_end.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDto {
    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;
    @NotBlank(message = "연락처는 필수입니다.")
    private String phone;

}
