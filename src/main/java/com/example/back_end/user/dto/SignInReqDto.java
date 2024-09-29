package com.example.back_end.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class SignInReqDto {
    @NotNull
    private String loginId;
    @NotNull
    private String password;
}
