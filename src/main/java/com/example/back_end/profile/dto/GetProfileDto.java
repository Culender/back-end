package com.example.back_end.profile.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProfileDto {
    private String nickname;
    private String profileImg;
}
