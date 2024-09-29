package com.example.back_end.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDto {
    @NotNull
    private String loginId;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private String nickname;
    @NotNull
    private String phone;

}
